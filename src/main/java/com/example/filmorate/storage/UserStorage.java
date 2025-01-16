package com.example.filmorate.storage;

import com.example.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Collection<User> findAll();

    User create(User user);

    int update(User user);

    Optional<User> findById(int userId);

    void addFriend(int userId, int friendId);

    List<User> findAllFriends(int userId);

    void deleteById(int userId, int friendId);

    List<User> getCommonFriends(int id, int otherId);
}
