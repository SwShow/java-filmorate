package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> findById(Long id);

    List<Film> findAllFilms();

    List<Film> findPopular(int count);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    void clearTableFilms();
}
