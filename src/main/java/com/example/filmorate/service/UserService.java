package com.example.filmorate.service;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userDbStorage;

    public Collection<User> findAll(){
        return userDbStorage.findAll();
    }

    public User create(User user){

        if (user.getName() == null) {
            user.setName(user.getEmail());
        }
        return userDbStorage.create(user);
    }

    public User update(User user){

        int updatedRows = userDbStorage.update(user);

        if (updatedRows == 0) {
            throw new NotFoundException("Пользователь не найден");
        }

        return user;

    }

    public User findById(int userId){
        return userDbStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void addFriend(int userId, int friendId){
        userDbStorage.addFriend(
                findById(userId).getId(),
                findById(friendId).getId()
        );

    }

    public List<User> findAllFriends(int userId) {
        findById(userId);
        return userDbStorage.findAllFriends(userId);
   }



    public void deleteFriend(int userId, int friendId) {
        userDbStorage.deleteById(
                findById(userId).getId(),
                findById(friendId).getId()
        );
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userDbStorage.getCommonFriends(id, otherId);
    }
}
