package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;

class UserControllerTest {

   /* UserController userController = new UserController(userService);

    @BeforeEach
    void clearRepository() {
        userController.userRepository.clear();
    }

    @Test
    void createUserWhenDataRight() {
        User user = new User("Email1@mail.ru", "LOGIN1", "NAME1", LocalDate.of(2011, 11, 1));
        User user2 = userController.createUser(user);
        assertEquals(1, userController.userRepository.size());
        assertEquals(user2.getName(), user.getName());
    }

    @Test
    void createUserWithoutEmail() {
        assertThrows(ValidationException.class,
                () -> userController.createUser(new User("", "LOGIN1", "NAME1", LocalDate.of(2011, 11, 1))));
    }

    @Test
    void createUserWrongEmail() {
        assertThrows(ValidationException.class,
                () -> userController.createUser(new User("Email1mail.ru", "LOGIN1", "NAME1", LocalDate.of(2011, 11, 1))));
    }

    @Test
    void createUserWithNullLogin() {
        assertThrows(ValidationException.class,
                () -> userController.createUser(new User("Email@1mail.ru", "", "NAME1", LocalDate.of(2011, 11, 1))));
    }

    @Test
    void createUserWithWhitespaceLogin() {
        assertThrows(ValidationException.class,
                () -> userController.createUser(new User("Email@1mail.ru", "LOG IN1", "NAME1", LocalDate.of(2011, 11, 1))));
    }

    @Test
    void createUserNoName() {
     User user2 =  userController.createUser(new User("Email@1mail.ru", "LOGIN1", "", LocalDate.of(2011, 11, 1)));
        assertEquals("LOGIN1", user2.getName());
    }

    @Test
    void createUserWithWrongBirthday() {
        assertThrows(ValidationException.class,
                () -> userController.createUser(new User("Email1@mail.ru", "LOGIN1", "NAME1", LocalDate.now().plusDays(1))));
    }

    @Test
    void getUserAndChangeRepository() {
       User user1 = userController.createUser(new User("Email1@mail.ru", "LOGIN1", "NAME1", LocalDate.of(2011, 11, 1)));
       User user2 = userController.createUser(new User("Email2@mail.ru", "LOGIN2", "NAME2", LocalDate.of(2022, 2, 2)));
       List<User> users = userController.getUsers();
       assertEquals(2, userController.userRepository.size());
       assertTrue(users.contains(user1));
       assertTrue(users.contains(user2));
    }*/

}