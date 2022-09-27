package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NoSuchUserException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film findFilmById(Long filmId) {
        if (filmId < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        return filmStorage.findById(filmId);
    }

    public Film createFilm(Film film) {
        validateFieldsFilms(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFieldsFilms(film);
        if (film.getId() < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.findAllFilms();
    }

    public List<Film> findPopularFilms(Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }
        return filmStorage.findPopular(count);
    }

    public Film addLike(Long filmId, Long userId) {
        if (filmId < 0 || userId < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        return filmStorage.addLike(filmId, userId);
    }

    public Film removeLike(Long filmId, Long userId) {
        if (filmId < 0 || userId < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        return filmStorage.deleteLike(filmId, userId);
    }


    public void validateFieldsFilms(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Введите название фильма.");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Лимит описания 200 символов.");
        }

        if (LocalDate.of(1895, 12, 28).isAfter(film.getReleaseDate())) {
            throw new ValidationException("дата релиза должна быть не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной.");
        }
    }
}
