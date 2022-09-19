package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int id = 0;
    protected final Map<Integer, Film> filmRepository = new HashMap<>();

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("получен запрос на добавление нового фильма");
        validateFieldsFilms(film);
        film.setId(++id);
        filmRepository.put(id, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        log.info("получен запрос на изменение данных фильма");
        validateFieldsFilms(newFilm);
        if (newFilm.getId() < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        filmRepository.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("получен запрос на получение списка фильмов");
        return new ArrayList<Film>(filmRepository.values());
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

    public void clearFilmRepository() {
        filmRepository.clear();
    }
}
