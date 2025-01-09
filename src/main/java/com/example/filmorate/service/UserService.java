package com.example.filmorate.service;

import com.example.filmorate.model.User;
import com.example.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAll(){
        return userStorage.findAll();
    }

    public User create(User user){

        if (user.getName() == null) {
            user.setName(user.getEmail());
        }
        return userStorage.create(user);
    }

    public User update(User user){
        return userStorage.update(user);
    }


    public List<User> findAllFriends(int userId) {
        return null;
    }

    public void addFriend(int userId, int friendId){
        userStorage.findById(userId).orElseThrow().getFriends().add(friendId);
    }
}
