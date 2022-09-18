package ru.yandex.practicum.filmorate.controller;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController = new FilmController();

    @BeforeEach
    void clearRepository() {
    filmController.clearFilmRepository();
}

    @Test
    void createFilmIfOllDataRight() {
        Film film = new Film("Cinema1", "Best1", LocalDate.of(1998, 10, 15), 90);
        Film newFilm = filmController.createFilm(film);
        assertEquals(1, filmController.filmRepository.size());
        assertEquals(film.getName(), newFilm.getName());
    }

    @Test
    void createFilmWhenNoName() {
        assertThrows(ValidationException.class,
                () -> filmController.createFilm(new Film("", "Best1",
                        LocalDate.of(1998, 10, 15), 90)));
    }

    @Test
    void createFilmWhenDescriptionVeryBig() {
        assertThrows(ValidationException.class,
                () -> filmController.createFilm(new Film("Cinema1", RandomString.make(201),
                        LocalDate.of(1998, 10, 15), 90)));
    }

    @Test
    void createFilmWhenWrongRelease() {
        assertThrows(ValidationException.class,
                () -> filmController.createFilm(new Film("Cinema1", "Best1",
                        LocalDate.of(1895, 12, 28).minusDays(1), 90)));
    }
    @Test
    void createFilmWhenWrongDuration() {
        assertThrows(ValidationException.class,
                () -> filmController.createFilm(new Film("Cinema1", "Best1",
                        LocalDate.of(1998, 10, 15), -90)));
    }


    @Test
    void getFilmsOfFilmRepository() {
        Film film = new Film("Cinema1", "Best1", LocalDate.of(1998, 10, 15), 90);
        filmController.createFilm(film);
        Film film2 = new Film("Cinema2", "Best2", LocalDate.of(2008, 5, 24), 180);
        filmController.createFilm(film2);
        List<Film> films = filmController.getFilms();
        assertEquals(2, filmController.filmRepository.size());
        assertTrue(films.contains(film));
        assertTrue(films.contains(film2));
    }
}