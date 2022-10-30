package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findById(Long id);

    List<User> findUsers();

    void addFriend(Long id, Long idFriend);

    void removeFriend(Long id, Long idFriend);

    List<User> findUserFriends(Long id);

    List<User> findCommonFriends(Long id, Long otherId);

    void clearTableUsers();

}
