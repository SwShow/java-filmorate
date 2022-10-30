package ru.yandex.practicum.filmorate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class NoSuchException extends RuntimeException {
        public NoSuchException(String message) {
            super(message);
        }
    }