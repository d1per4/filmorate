package com.example.filmorate.storage.impl;

import com.example.filmorate.model.Film;
import com.example.filmorate.storage.FilmStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> params = Map.of(
                "NAME", film.getName(),
                "DESCRIPTION", film.getDescription(),
                "RELEASE_DATE", film.getReleaseDate(),
                "DURATION", film.getDuration()
        );

        int id = insert.executeAndReturnKey(params).intValue();
        film.setId(id);
        return film;
    }

    @Override
    public int update(Film film) {
        String sql = "update FILMS set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ? where ID = ?";
        return jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
                );
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    @Override
    public Optional<Film> findById(int filmId) {
        String sql = "SELECT * FROM FILMS WHERE ID = ?";
        return jdbcTemplate.query(sql, this::mapRow, filmId)
                .stream()
                .findFirst();
    }

    @Override
    public void addLike(int filmId, int userId) {

    }

    @Override
    public void removeLike(int filmId, int userId) {

    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .build();
    }

}
