package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

@Data
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private TreeSet<Genre> genres;
    private Mpa mpa;


    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

}
