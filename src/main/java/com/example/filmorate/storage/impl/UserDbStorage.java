package com.example.filmorate.storage.impl;

import com.example.filmorate.model.User;
import com.example.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAll() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public User create(User user) {

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> map = Map.of(
                "EMAIL", user.getEmail(),
                "LOGIN", user.getLogin(),
                "NAME", user.getName(),
                "BIRTHDAY", user.getBirthday()
        );

        int id = insert.executeAndReturnKey(map).intValue();
        user.setId(id);
        return user;
    }

    @Override
    public int update(User user) {
        String sql = "update USERS set EMAIl = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? where ID = ?";
        return jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
    }

    @Override
    public Optional<User> findById(int userId) {
        String sql = "select * from USERS where ID = ?";
        return jdbcTemplate.query(sql, this::mapRow, userId)
                .stream()
                .findFirst();
    }

    @Override
    public void addFriend(int userId, int friendId){
        String sql = "insert into FRIENDS (USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> findAllFriends(int userId){
        String sql = """
                select * from FRIENDS
                join USERS on FRIEND_ID = ID
                where USER_ID = ?
                """;
        return jdbcTemplate.query(sql, this::mapRow, userId);
    }

    @Override
    public void deleteById(int userId, int friendId) {
        String sql = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId){
        String sql = """
                select * from FRIENDS F1
                join FRIENDS F2 on F1.FRIEND_ID = F2.FRIEND_ID
                join USERS U on F1.FRIEND_ID = U.ID
                where F1.USER_ID = ? and F2.USER_ID = ?
                """;
        return jdbcTemplate.query(sql, this::mapRow, id, otherId);
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {

        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();

    }
}
