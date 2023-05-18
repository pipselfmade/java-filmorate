package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;


    public User createUser(User user) {
        validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        Optional<User> optionalUser = userStorage.updateUser(user);
        validate(user);
        if (optionalUser.isEmpty()) {
            log.error("Incorrect ID error: User with this ID does not exist when updating");
            throw new ObjectNotFoundException("User with this ID does not exist when updating");
        }

        log.info("User '" + user.getName() + "' updated successfully");
        return optionalUser.get();
    }

    public User deleteUser(Integer userId) {
        Optional<User> optionalUser = userStorage.deleteUser(userId);

        if (optionalUser.isEmpty()) {
            log.debug("Incorrect ID error: User with this ID does not exist when deleting");
            throw new ObjectNotFoundException("User with this ID does not exist when deleting");
        }

        log.debug("User '" + getUserById(userId).getName() + "' delete successfully");
        return optionalUser.get();
    }


    public List<User> getAllUsers() {
        log.info("All users returned successfully");
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer userId) {
        Optional<User> optionalUser = userStorage.getUserById(userId);

        if (optionalUser.isEmpty()) {
            log.error("Incorrect ID error: User with ID '" + userId + "' does not exist when getting by ID");
            throw new ObjectNotFoundException("User with ID '" + userId + "' does not exist when getting by ID");
        }

        log.info("User with ID '" + userId + "' returned successfully");
        return optionalUser.get();
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user == null | friend == null) {
            log.error("Incorrect ID error: Users do not exist when adding friend");
            throw new ObjectNotFoundException("Users do not exist when adding friend");
        }

        friendStorage.addFriend(userId, friendId);
        log.info("Friend with ID '" + friendId + "' successfully added to user '" + userId + "'");
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user == null | friend == null) {
            log.error("Incorrect ID error: Users do not exist when deleting friend");
            throw new ObjectNotFoundException("Users do not exist when deleting friend");
        }
        if (!user.getFriendsIds().contains(friendId)) {
            log.error("Incorrect ID error: User has not friend with id '" + friendId + "' when deleting friend");
            throw new ObjectNotFoundException("User has not friend with id '" + friendId + "' when deleting friend");
        }

        friendStorage.deleteFriend(userId, friendId);
        log.info("Friend with ID '" + friendId + "' successfully removed from user '" + userId + "' friends list");
    }

    public List<User> getAllFriends(Integer userId) {
        List<User> users = userStorage.getAllFriends(userId);
        log.info("All friends returned successfully");
        return users;
    }

    public Set<User> getCommonFriendsIds(Integer userId, Integer otherUserId) {
        Set<User> commonFriends = userStorage.getCommonFriendsIds(userId, otherUserId);
        log.debug("Common friends returned successfully");
        return commonFriends;
    }

    public static void validate(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }

        if (user.getEmail().isBlank()) {
            log.error("Validation error: Email name can't be blank");
            throw new ValidationException("Email name can't be blank");
        }

        if (!user.getEmail().contains("@")) {
            log.error("Validation error: Email name should contain '@'");
            throw new ValidationException("Email name should contain '@'");
        }

        if (user.getLogin().isBlank()) {
            log.error("Validation error: Login can't be blank");
            throw new ValidationException("Login can't be blank");
        }

        if (user.getLogin().contains(" ")) {
            log.error("Validation error: Login can't contain spaces");
            throw new ValidationException("Login can't contain spaces");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Validation error: Users birthday can't be in the future");
            throw new ValidationException("Users birthday can't be in the future");
        }
    }
}
