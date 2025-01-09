package com.example.filmorate.storage.impl;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.UserStorage;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int uniqueId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(getUniqueId());

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден");
        }
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.ofNullable(users.get(userId));
    }


    private int getUniqueId() {
        return uniqueId++;
    }


}
