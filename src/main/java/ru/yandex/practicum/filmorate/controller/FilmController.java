package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/{id}")
    public Optional<Film> findFilmById(@PathVariable Long id) {
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
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }


}
