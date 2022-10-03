package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User createUser(User user);
    User updateUser(User user);
    User findById(Long id);
    Collection<User> findUsers();
    List<User> findUserFriends(Long id);
    List<User> findCommonFriends(Long id, Long otherId);
    void addFriend(Long userId, Long friendId);
    void removeFriend(Long userId, Long friendId);
}
