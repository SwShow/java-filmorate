package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.join;

@Component
public class InMemoryUserStorage implements UserStorage{
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private Long id = 0L;
    protected final Map<Long, User> userRepository = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++id);
        userRepository.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        if (id < 0) {
            throw new NoSuchUserException("Идентификатор должен быть положительнымю");
        }
        userRepository.replace(id, user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return userRepository.get(id);
    }

    @Override
    public Collection<User> findUsers() {
        return userRepository.values();
    }

    @Override
    public List<User> findUserFriends(Long id) {
        Set<Long> userFriendsId = userRepository.get(id).getFriends();
        List<User> usFriends = new ArrayList<>();
        for (Long idFr : userFriendsId) {
            usFriends.add(userRepository.get(idFr));
        }
        return usFriends;
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        Set<Long> usId = userRepository.get(id).getFriends();
      Set<Long> usOtherId = userRepository.get(otherId).getFriends();
      usId.retainAll(usOtherId);
      List<User> users = new ArrayList<>();
      for(Long i : usId) {
          users.add(userRepository.get(i));
      }
      return users;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
    User user1 = userRepository.get(userId);
    User user2 = userRepository.get(friendId);
    user1.getFriends().add(friendId);
    userRepository.put(userId, user1);
    user2.getFriends().add(userId);
    userRepository.put(friendId, user2);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        User user1 = userRepository.get(id);
        User user2 = userRepository.get(friendId);
        user1.getFriends().remove(friendId);
        userRepository.put(id, user1);
        user2.getFriends().remove(id);
        userRepository.put(friendId, user2);
    }

}
