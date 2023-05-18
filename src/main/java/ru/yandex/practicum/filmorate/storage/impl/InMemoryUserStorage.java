package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            return Optional.empty();
        }

        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        if (!users.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(users.get(id));
    }

    @Override
    public Optional<User> deleteUser(Integer userId) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        return null;
    }

    @Override
    public Set<User> getCommonFriendsIds(Integer userId, Integer otherUserId) {
        return null;
    }

    @Override
    public void deleteAllUsers() {
    }
}
