package com.example.filmorate.service;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.exception.UserAlreadyExistsException;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        if(!(user.getFriends().contains(friendId))){
            user.getFriends().add(friendUser.getId());
            friendUser.getFriends().add(user.getId());
            update(user);
            update(friendUser);
        } else {
            throw new UserAlreadyExistsException("Пользователь уже добавлен в друзья");
        }

    }

    public List<User> findAllFriends(int userId) {
        return findById(userId).getFriends()
                .stream()
                .map(this::findById)
                .toList();
    }

    public void deleteFriend(int userId, int friendId) {
        User user = findById(userId);
        User friendUser = findById(friendId);
        if(user.getFriends().contains(friendId)){
            user.getFriends().remove(friendUser.getId());
            friendUser.getFriends().remove(user.getId());
            update(user);
            update(friendUser);
        }
    }

    public List<User> getCommonFriends(int id, int otherId) {
        Set<Integer> friends = new HashSet<>(findById(id).getFriends());
        friends.retainAll(findById(otherId).getFriends());

        return friends.stream()
                .map(this::findById)
                .toList();
    }
}
