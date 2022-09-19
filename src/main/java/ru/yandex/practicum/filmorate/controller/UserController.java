package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int id = 0;
    protected final Map<Integer, User> userRepository = new HashMap<>();

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("получен запрос на создание пользователя");
        validateFieldsUser(user);
        user.setId(++id);
        userRepository.put(id, user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("получен запрос на изменение данных пользователя");
        validateFieldsUser(user);
        int id = user.getId();
        if (id < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        userRepository.replace(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("получен запрос на получение данных пользователей");
        return new ArrayList<>(userRepository.values());
    }

    public void validateFieldsUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Введите корректный адрес.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Введите корректный логин, не содержащий пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            throw new ValidationException("Введите корректную дату");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }

    public void clearRepository() {
        userRepository.clear();
    }

}
