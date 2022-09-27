package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.NoSuchUserException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Long id = 0L;
    protected final Map<Long, Film> filmRepository = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(++id);
        filmRepository.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        filmRepository.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film findById(Long id) {
        return filmRepository.get(id);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return filmRepository.values();
    }

    @Override
    public List<Film> findPopular(int count) {
        int count1 = count;
        if (count < filmRepository.size()) {
            count1 = filmRepository.size();
        }
        Collection<Film> films = filmRepository.values();
        return films.stream()
                .sorted(Comparator.comparingInt(Film::getQuanLikes).reversed())
                .limit(count1)
                .collect(Collectors.toList());


    }


    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = filmRepository.get(filmId);
        film.getLikes().add(userId);
        log.info("лайк добавлен:" + film.getLikes());
        filmRepository.replace(filmId, film);
        return film;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmRepository.get(filmId);
        film.getLikes().remove(userId);
        log.info("лайк удален:" + film.getLikes());
        filmRepository.replace(filmId, film);
        return film;
    }


}
