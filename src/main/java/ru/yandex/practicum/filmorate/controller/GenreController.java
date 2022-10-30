package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreStorage genreStorage;

    @GetMapping("/{id}")
    public Genre getById(@PathVariable int id) {
        Optional<Genre> genre = genreStorage.getById(id);
        if (genre.isEmpty()) {
            throw new NotFoundException("По указанному id не найден жанр");
        } else {
            log.info("запрошен жанр id{}", id);
            return genre.get();
        }
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("запрошены все жанры");
        return genreStorage.findAll();
    }
}
