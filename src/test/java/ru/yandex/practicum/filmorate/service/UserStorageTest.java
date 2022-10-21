package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTest {
    public final UserService userService;
    public final UserStorage userStorage;

    @BeforeEach
    void clearRepository() {
        userStorage.clearTableUsers();
    }

    @Test
    void createUserWhenDataRight() {
        User user = userService.createUser(new User(0,"Email1@mail.ru",  "NAME1","LOGIN1",
                LocalDate.of(2011, 11, 1)));
        assertEquals(1, user.getId());
        assertEquals("NAME1", user.getName());
    }
    @Test
    void createUserWithoutEmail() {
        assertThrows(ValidationException.class,
                () -> userService.createUser(new User(0,"",  "NAME1","LOGIN1", LocalDate.of(2011, 11, 1))));
    }
    @Test
    void createUserWrongEmail() {
        assertThrows(ValidationException.class,
                () -> userService.createUser(new User(0,"Email1mail.ru",  "NAME1", "LOGIN1", LocalDate.of(2011, 11, 1))));
    }
    @Test
    void createUserWithNullLogin() {
        assertThrows(ValidationException.class,
                () -> userService.createUser(new User(0,"Email@1mail.ru", "NAME1", "", LocalDate.of(2011, 11, 1))));
    }
    @Test
    void createUserWithWhitespaceLogin() {
        assertThrows(ValidationException.class,
                () -> userService.createUser(new User(0,"Email@1mail.ru", "NAME1","LOG IN1",  LocalDate.of(2011, 11, 1))));
    }
    @Test
    void createUserNoName() {
        User user2 =  userService.createUser(new User(0,"Email@1mail.ru", "","LOGIN1",  LocalDate.of(2011, 11, 1)));
        assertEquals("LOGIN1", user2.getName());
    }
    @Test
    void createUserWithWrongBirthday() {
        assertThrows(ValidationException.class,
                () -> userService.createUser(new User(0,"Email1@mail.ru", "NAME1","LOGIN1",  LocalDate.now().plusDays(1))));
    }
    @Test
    void createAndGetUsers() {
        User user3 = userService.createUser(new User(0,"Email1@mail.ru", "NAME1","LOGIN1",
                LocalDate.of(2011, 11, 1)));
        User user4 = userService.createUser(new User(0,"Email2@mail.ru", "NAME2","LOGIN2",
                LocalDate.of(2022, 2, 2)));
        user3.addFriend(2L);
        Collection<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertTrue(user3.getFriends().contains(2L));
        assertFalse(user4.getFriends().contains(2L));
    }

}