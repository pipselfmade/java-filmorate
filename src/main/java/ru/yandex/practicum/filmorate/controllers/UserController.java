package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    private User createUser(@RequestBody User user) throws ValidationException {
        validate(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("User '" + user.getName() + "' created successfully");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException, IncorrectIdException {
        validate(user);

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("User '" + user.getName() + "' updated successfully");
            return user;
        } else {
            log.debug("Incorrect id");
            throw new IncorrectIdException("Incorrect id");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public static void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank()) {
            log.debug("Validation error: Email name can't be blank");
            throw new ValidationException("Email name can't be blank");
        }

        if (!user.getEmail().contains("@")) {
            log.debug("Validation error: Email name should contain '@'");
            throw new ValidationException("Email name should contain '@'");
        }

        if (user.getLogin().isBlank()) {
            log.debug("Validation error: Login can't be blank");
            throw new ValidationException("Login can't be blank");
        }

        if (user.getLogin().contains(" ")) {
            log.debug("Validation error: Login can't contain spaces");
            throw new ValidationException("Login can't contain spaces");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Validation error: Users birthday can't be in the future");
            throw new ValidationException("Users birthday can't be in the future");
        }
    }
}
