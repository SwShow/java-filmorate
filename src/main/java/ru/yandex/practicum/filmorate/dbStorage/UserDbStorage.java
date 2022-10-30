package ru.yandex.practicum.filmorate.dbStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.Exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private int userId = 0;

    private int createUserId() {
        return ++userId;
    }

    @Override
    public User createUser(User user) {
        log.info("Создание пользователя");
        if (user.getName().equals("") || user.getName() == null) {
            user.setName(user.getLogin());
        }

        int id = createUserId();
        jdbcTemplate.update("INSERT INTO users(id_user, name, login, email, birthday) values(?,?,?,?,?)"
                , id, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT u.id_user,f.id_friend " +
                "FROM users u "
                + "LEFT JOIN friends f on u.id_user = F.id_friend "
                + "WHERE u.id_user=?", id);
        while (userRow.next()) {
            user.setId(userRow.getLong("id_user"));
            user.addFriend(userRow.getLong("id_friend"));
        }
        log.info("пользователь создан:" + user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Новые данные пользователя " + user);
        jdbcTemplate.update("UPDATE users SET name=?,login=?,email=?,birthday=? " +
                        "WHERE id_user=?", user.getName(), user.getLogin(), user.getEmail(), user.getBirthday()
                , user.getId());
        log.info("Пользователь создан, возврат");
        return findById(user.getId()).get();
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("Поиск пользователя с идентификатором:" + id);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
            log.info("Пользователь найден, " + user);
            return Optional.of(user);
        } else {
            throw new NotFoundException("user not found");
        }
    }


    @Override
    public List<User> findUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public void addFriend(Long id, Long idFriend) {
        jdbcTemplate.update("INSERT INTO friends(id_user,id_friend,status) values(?,?,1)", id, idFriend);
    }

    @Override
    public void removeFriend(Long id, Long idFriend) {
        jdbcTemplate.update("DELETE FROM friends WHERE id_user=? and id_friend=?", id, idFriend);
    }

    @Override
    public List<User> findUserFriends(Long id) {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT u.* FROM friends f  "
                + "JOIN users u on f.id_friend = u.id_user "
                + "WHERE f.status = 1 and f.id_user=?", id);

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user IN "
                + "(SELECT id_friend FROM friends WHERE id_user=? AND id_friend IN "
                + "(SELECT id_friend FROM friends WHERE id_user=?))", id, otherId);

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("name"),
                    userRows.getString("login"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }
    @Override
    public void clearTableUsers() {
        userId = 0;
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM friends");
    }


}
