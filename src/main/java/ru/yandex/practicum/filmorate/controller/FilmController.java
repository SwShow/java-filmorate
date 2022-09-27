package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Long id) {
        log.info("Запрос на получение фильма по идентификатору");
        return filmService.findFilmById(id);
    }

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.info("получен запрос на добавление нового фильма");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film newFilm) {
        log.info("получен запрос на изменение данных фильма");
        return filmService.updateFilm(newFilm);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("получен запрос на получение списка фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(required = false) Integer count) {
        log.info("запрос популярных фильмов, count=" + count);
            return filmService.findPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.removeLike(id, userId);
    }

}
