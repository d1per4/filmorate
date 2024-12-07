package com.example.filmorate.controller;

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
    public Collection<User> findAll(){
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user){
        user.setId(getUniqueId());
        if(user.getName() == null){
            user.setName(user.getEmail());
        }

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(Integer id, @RequestBody User user){
        users.get(id).setId(user.getId());
        return user;
    }

    private int getUniqueId(){
        return uniqueId++;
    }

}
