package ru.yandex.practicum.filmorate.controller;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 0;
    protected final Map<Integer, Film> filmRepository = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping()
    public Film createFilm(@RequestBody Film film) {
        log.info("получен запрос на добавление нового фильма");
        film.setId(++id);
        filmRepository.put(id, film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film newFilm) throws NoSuchUserException {
        log.info("получен запрос на изменение данных фильма");
        if (newFilm.getId() < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
            filmRepository.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("получен запрос на получение списка фильмов");
        return new ArrayList<Film>(filmRepository.values());
    }
    public void clearFilmRepository() {
        filmRepository.clear();
    }
}
