package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dbStorage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;
    @BeforeEach
    public void beforeEach() {
        User user1 = new User(1, "user1@example", "User1"
                , "user1", LocalDate.of(2000, 1, 1));
        User user2 = new User(2, "user2@example", "User2"
                , "user2", LocalDate.of(2000, 2, 1));
        userStorage.createUser(user1);
        userStorage.createUser(user2);
    }
    @Test
    public void testFindUserById() {

        userStorage.createUser(new User(1,"Tame@mail.su", "Tame", "Login",
                LocalDate.of(2002, 11, 15)));
        Optional<User> userOptional = userStorage.findById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

}
