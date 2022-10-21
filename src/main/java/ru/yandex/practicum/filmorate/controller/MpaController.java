package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaStorage mpaStorage;

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable int id) {
        log.info("запрос рейтинга по id= " + id);
        Optional<Mpa> mpa = mpaStorage.getById(id);
        if (mpa.isEmpty()) {
            throw new NotFoundException("По id= " + id + "рейтинг не найден");
        } else {
            return mpa.get();
        }
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("запрошены все рейтинги");
        return mpaStorage.findAll();
    }
}
