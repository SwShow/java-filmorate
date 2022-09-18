package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.ValidationException;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    @NotBlank
    @Size(min = 1, max = 200)
    @NotNull
    @Min(1)
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Введите название фильма.");
        }

        if (description.length() > 200) {
            throw new ValidationException("Лимит описания 200 символов.");
        }

        if (LocalDate.of(1895, 12, 28).isAfter(releaseDate)) {
            throw new ValidationException("дата релиза должна быть не раньше 28 декабря 1895 года");
        }

        if (duration <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной.");
        }
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
