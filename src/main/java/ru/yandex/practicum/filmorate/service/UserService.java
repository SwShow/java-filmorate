package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NoSuchUserException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validateFieldsUser(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateFieldsUser(user);
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.findUsers();
    }

    public User getUserById(Long id) {
        if (id < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        return userStorage.findById(id);
    }

    public void addFriends(Long id, Long friendId) {
        if (id < 0 || friendId < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriends(Long id, Long friendId) {
        if (id < 0 || friendId < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        userStorage.removeFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        if (id < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        return userStorage.findUserFriends(id);
    }

    public List<User> getCommonFrs(Long id, Long otherId) {
        if (id < 0 || otherId < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        return userStorage.findCommonFriends(id, otherId);
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


}
