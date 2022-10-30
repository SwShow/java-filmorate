package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @BeforeEach
    void clearRepository() {
        filmStorage.clearTableFilms();
    }

    @Test
    void createFilmIfNotMpa() {
        assertThrows(ValidationException.class,
                () -> filmService.createFilm(new Film(0L,"Cinema1", "Best1",
                LocalDate.of(1998, 10, 15), 90, 2)));
    }
    @Test
    void createFilmWhenNoName() {
        assertThrows(ValidationException.class,
                () -> filmService.createFilm(new Film(0L,"", "Best1",
                        LocalDate.of(1998, 10, 15), 90, 1)));
    }
    @Test
    void createFilmWhenDescriptionVeryBig() {
        assertThrows(ValidationException.class,
                () -> filmService.createFilm(new Film(0L,"Cinema1", RandomString.make(201),
                        LocalDate.of(1998, 10, 15), 90, 3)));
    }
    @Test
    void createFilmWhenWrongRelease() {
        assertThrows(ValidationException.class,
                () -> filmService.createFilm(new Film(0L,"Cinema1", "Best1",
                        LocalDate.of(1895, 12, 28).minusDays(1), 90, 2)));
    }
    @Test
    void createFilmWhenWrongDuration() {
        assertThrows(ValidationException.class,
                () -> filmService.createFilm(new Film(0L,"Cinema1", "Best1",
                        LocalDate.of(1998, 10, 15), -90, 1)));
    }
    @Test
    void createFilmOllRight() {
        Film film = new Film(0L,"Cinema1", "Best1",
                LocalDate.of(1998, 10, 15), 90,  2);
        film.setMpa(new Mpa(5));
        Film film2 = filmService.createFilm(film);
        assertEquals(1L, film2.getId());
        assertEquals("Cinema1", film2.getName());
        assertEquals("NC-17", film2.getMpa().getName());
       
    }

}