package com.example.filmorate.controller;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.User;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private int uniqueId = 1;

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId(getUniqueId());
        if (user.getName() == null) {
            user.setName(user.getEmail());
        }

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/{id}")
    public User update(@Valid @RequestBody User user) {
        User updateUser = users.get(user.getId());

        if (updateUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        users.put(user.getId(), updateUser);

        return updateUser;
    }

    private int getUniqueId() {
        return uniqueId++;
    }


}
