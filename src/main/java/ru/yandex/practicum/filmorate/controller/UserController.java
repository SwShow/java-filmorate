package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;


@RestController
@RequestMapping("/users")
public class UserController {
   private int id = 0;
   protected final Map<Integer, User> userRepository = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping()
    public User createUser(@RequestBody User user) {
        log.info("получен запрос на создание пользователя");
        user.setId(++id);
        userRepository.put(id, user);
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user)throws NoSuchUserException  {
        log.info("получен запрос на изменение данных пользователя");
        int id = user.getId();
        if (id < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        if (!userRepository.containsKey(id)) {
            createUser(user);
        } else {
            userRepository.replace(user.getId(), user);
        }
        return user;
    }

    @GetMapping()
    public List<User> getUsers() {
        log.info("получен запрос на получение данных пользователей");
        return new ArrayList<>(userRepository.values());
    }

public void clearRepository() {
        userRepository.clear();
}

}
