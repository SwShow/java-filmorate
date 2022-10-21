package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exception.NoSuchException;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Optional<Film> findFilmById(Long filmId) {
        if (filmId < 0) {
            throw new NoSuchException("Идентификатор должен быть положительнымю");
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
            throw new NoSuchException("Идентификатор должен быть положительнымю");
        }
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.findAllFilms();
    }

    public List<Film> findPopularFilms(Integer count) {
        if (count == null || count == 0 || count > 10) {
            count = 10;
        }
        log.info("Поиск популярных фильмов, количество: " + count);
        return filmStorage.findPopular(count);
    }

    public void addLike(Long filmId, Long userId) {
        log.info("Пполучен запрос не добавление лайка фильму:" + filmId + "пользователем" + userId);
        if (filmId < 0 || userId < 0) {
            throw new NoSuchException("Идентификатор должен быть положительнымю");
        }
        if (filmStorage.findById(filmId).isEmpty() || userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("По указанным id не найден фильм или пользователь");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (filmId < 0 || userId < 0) {
            throw new NotFoundException("not found");
        }
        if (filmStorage.findById(filmId).isEmpty() || userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("По указанным id не найден фильм или пользователь");
        }
        filmStorage.deleteLike(filmId, userId);
    }

    public void validateFieldsFilms(Film film) throws ValidationException {
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
        if (film.getMpa() == null) {
            throw new ValidationException("Mpa фильма не должно равняться null.");
        }
    }


}
