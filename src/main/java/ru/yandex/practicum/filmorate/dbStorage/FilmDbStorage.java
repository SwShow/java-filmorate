package ru.yandex.practicum.filmorate.dbStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private int filmId = 0;

    private int createFilmId() {
        return ++filmId;
    }

    @Override
    public Film createFilm(Film film) {
        int id = createFilmId();

        jdbcTemplate.update("INSERT INTO films(id_film, name, description, release_date, duration, rate, mpa) " +
                        "values(?,?,?,?,?,?,?)", id, film.getName(), film.getDescription(), film.getReleaseDate()
                , film.getDuration(), film.getRate(), film.getMpa().getId());
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(
                "SELECT f.id_film ,m.name_rate " +
                        "FROM films f " +
                        "JOIN mpa_rating m ON f.mpa = m.id_rate " +
                        "WHERE f.id_film = ?", id);
        if (mpaRow.next()) {
            film.getMpa().setName(mpaRow.getString("name_rate"));
            film.setId(mpaRow.getLong("id_film"));
        }

        TreeSet<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update("MERGE INTO film_genre_join (id_film, id_genre) KEY (id_film, id_genre) " +
                        "VALUES (?, ?)", film.getId(), genre.getId());
            }
        }
        log.info("Создан фильм: " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Изменение данных фильма:" + film);
        Long idFilm = film.getId();
        findById(idFilm);
        jdbcTemplate.update("DELETE FROM film_genre_join WHERE id_film=?", idFilm);
        jdbcTemplate.update("UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?" +
                        ", mpa = ? WHERE id_film = ?", film.getName(), film.getDescription(), film.getReleaseDate()
                , film.getDuration(), film.getRate(), film.getMpa().getId(), idFilm);

        TreeSet<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update("MERGE INTO film_genre_join (id_film, id_genre) KEY (id_film, id_genre) " +
                        "VALUES (?, ?)", idFilm, genre.getId());
            }
        }

        return findById(film.getId()).get();
    }

    @Override
    public Optional<Film> findById(Long id) {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.id_film,f.name,f.description," +
                "f.release_date,f.duration,f.rate,f.mpa,m.id_rate,m.name_rate " +
                "FROM films f " +
                "JOIN mpa_rating m ON m.id_rate = f.mpa WHERE id_film=?", id);

        if (filmRows.next()) {
            Film film = makeFilm(filmRows);
            ArrayList<Genre> genres = new ArrayList<>();

            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres g " +
                    "JOIN film_genre_join f on g.id_genre = f.id_genre WHERE f.id_film = ?" +
                    "ORDER BY f.id_film, g.id_genre", id);

            while (genreRows.next()) {
                genres.add(new Genre(genreRows.getInt("id_genre"), genreRows.getString("name_genre")));
            }
            film.setGenres(new TreeSet<>(genres));
            log.info("получен фильм: " + film);
            return Optional.of(film);
        } else {
            throw new NotFoundException("film not found");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.id_film,f.name,f.description," +
                "f.release_date,f.duration,f.rate,f.mpa,m.id_rate,m.name_rate " +
                "FROM films f " +
                "JOIN mpa_rating m ON m.id_rate=f.mpa");

        while (filmRows.next()) {
            films.add(makeFilm(filmRows));
        }

        addGenres(films);

        return films;
    }

    @Override
    public List<Film> findPopular(int count) {
        List<Film> films = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT f.id_film, f.name fn, f.description, f.duration, " +
                        "f.release_date, f.rate, f.mpa,m.id_rate,m.name_rate, COUNT(r.id_user) " +
                        "FROM films f " +
                        "LEFT JOIN rating r ON f.id_film = r.id_film " +
                        "JOIN mpa_rating m ON f.mpa=m.id_rate " +
                        "GROUP BY f.id_film " +
                        "ORDER BY COUNT(r.id_user) DESC " +
                        "LIMIT ?", count);

        while (filmRows.next()) {
            films.add(makeFilm(filmRows));
        }

        addGenres(films);
        log.info("popular films: " + films);
        return films;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("добавление лайка фильму" + filmId + "пользователем" + userId);
        jdbcTemplate.update("INSERT INTO rating(id_user, id_film) VALUES(?,?)", userId, filmId);
        log.info("Лайк добавлен фильму" + findById(filmId));
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbcTemplate.update("DELETE FROM rating WHERE id_user=? AND id_film=?", userId, filmId);
    }

    private Film makeFilm(SqlRowSet filmRows) {
        Long id = filmRows.getLong("id_film");
        Mpa mpa = new Mpa(filmRows.getInt("id_rate"), filmRows.getString("name_rate"));

        Film film = new Film(
                id,
                filmRows.getString("name"),
                filmRows.getString("description"),
                Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                filmRows.getInt("duration"),
                filmRows.getInt("rate")
        );
        film.setMpa(mpa);

        return film;
    }

    private void addGenres(List<Film> films) {
        ArrayList<Genre> genres = new ArrayList<>();

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres g " +
                "JOIN film_genre_join f on g.id_genre = f.id_genre " +
                "ORDER BY f.id_film, g.id_genre");

        while (genreRows.next()) {
            Long idFilm = genreRows.getLong("id_film");
            for (Film film : films) {
                if (film.getId().equals(idFilm)) {
                    genres.add(new Genre(genreRows.getInt("id_genre"), genreRows.getString("name_genre")));
                }
                film.setGenres(new TreeSet<>(genres));
            }
        }
    }
    @Override
    public void clearTableFilms() {
        filmId = 0;
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("DELETE FROM genres");
        jdbcTemplate.update("DELETE FROM rating");
        jdbcTemplate.update("DELETE FROM film_genre_join");
    }
}
