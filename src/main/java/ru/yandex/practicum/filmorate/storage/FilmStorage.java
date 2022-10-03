package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    Film findById(Long id);
    Collection<Film> findAllFilms();
    List<Film> findPopular(int count);
    Film addLike(Long filmId, Long userId);
    Film deleteLike(Long filmId, Long userId);
}
