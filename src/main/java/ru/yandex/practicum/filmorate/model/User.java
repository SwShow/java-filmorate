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
public class User {
    @NotBlank
    @Size(min = 1, max = 200)
    @NotNull
    @Min(1)
    private int id;
     private String email;
    private String login;
    private String name;
    private LocalDate birthday;


    public User(String email, String login, String name, LocalDate birthday) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Введите корректный адрес.");
        }
            if (login == null || login.isBlank() || login.contains(" ")) {
                throw new ValidationException("Введите корректный логин, не содержащий пробелы.");
            }
            if (birthday.isAfter(LocalDate.now())) {
                throw new ValidationException("Введите корректную дату");
            }
            this.email = email;
            this.login = login;
        if (name == null || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
            this.birthday = birthday;

    }

}
