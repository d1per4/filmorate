package com.example.filmorate.service;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
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

    public User findById(int userId){
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void addFriend(int userId, int friendId){
        User user = findById(userId);
        User friendUser = findById(friendId);

        if(user.getFriends().contains(friendUser.getId())) {
            System.out.println("Пользователь уже добавлен в друзья");
        }

        user.getFriends().add(friendUser.getId());
    }

    public List<User> findAllFriends(int userId) {
        return null;
    }
}
