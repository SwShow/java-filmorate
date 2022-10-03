package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.NoSuchUserException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 0L;
    protected final Map<Long, Film> filmStorage = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(++id);
        filmStorage.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        filmStorage.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film findById(Long id) {
        return filmStorage.get(id);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmStorage.values();
    }

    @Override
    public List<Film> findPopular(int count) {
        if (count > filmStorage.size()) {
            count = filmStorage.size();
        }
        Collection<Film> films = filmStorage.values();
        return films.stream()
                .sorted(Comparator.comparingInt(Film::getQuanLikes)
                       .reversed())
                .limit(count)
                .collect(Collectors.toList());
    }


    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.get(filmId);
        film.getLikes().add(userId);
        log.info("лайк добавлен:" + film.getLikes());
        return film;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.get(filmId);
        film.getLikes().remove(userId);
        log.info("лайк удален:" + film.getLikes());
        return film;
    }


}
