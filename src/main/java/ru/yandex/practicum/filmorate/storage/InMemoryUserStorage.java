package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.join;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Long id = 0L;
    protected final Map<Long, User> userStorage = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++id);
        userStorage.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        if (id < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        userStorage.replace(id, user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return userStorage.get(id);
    }

    @Override
    public Collection<User> findUsers() {
        return userStorage.values();
    }

    @Override
    public List<User> findUserFriends(Long id) {
        Set<Long> userFriendsId = userStorage.get(id).getFriends();
        log.info("user:"+ id + ",friendsId:" + userFriendsId);
        List<User> usFriends = new ArrayList<>();
        for (Long idFr : userFriendsId) {
            usFriends.add(userStorage.get(idFr));
        }
        return usFriends;
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        Set<Long> usId = userStorage.get(id).getFriends();
        Set<Long> usOtherId = userStorage.get(otherId).getFriends();
        usId.retainAll(usOtherId);
        List<User> users = new ArrayList<>();
        for (Long i : usId) {
            users.add(userStorage.get(i));
        }
        return users;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        log.info("addFriends:" + userId + " friendId:" + friendId);
        final User user1 = userStorage.get(userId);
        final User user2 = userStorage.get(friendId);
        user1.getFriends().add(friendId);
        user2.getFriends().add(userId);
        log.info("friends added:" + user1 + user2);

    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        User user1 = userStorage.get(id);
        User user2 = userStorage.get(friendId);
        user1.getFriends().remove(friendId);
        user2.getFriends().remove(id);
    }

}



















