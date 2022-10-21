package ru.yandex.practicum.filmorate.dbStorage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public Optional<Genre> getById(Integer id) {
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE ID_GENRE=?", id);
        if (genreRow.next()) {
            Genre genre = new Genre(
                    genreRow.getInt("id_genre"),
                    genreRow.getString("name_genre")
            );
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    public List<Genre> findAll() {
        List<Genre> listGen = new ArrayList<>();
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        while (genreRow.next()) {
            Genre genre = new Genre(
                    genreRow.getInt("id_genre"),
                    genreRow.getString("name_genre")
            );
            listGen.add(genre);
        }
        return listGen;
    }
}
