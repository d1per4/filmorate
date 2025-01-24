package com.example.filmorate.storage.impl;

import com.example.filmorate.model.Mpa;
import com.example.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, this::mapRowForMpa);
    }

    @Override
    public Optional<Mpa> getMpaById(int mpaId) {
        String sql = "SELECT * FROM MPA WHERE ID = ?";
        return jdbcTemplate.query(sql, this::mapRowForMpa, mpaId)
                .stream()
                .findFirst();
    }

    @Override
    public boolean mpaExists(Integer mpaId) {
        String sql = "SELECT COUNT(*) FROM MPA WHERE ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mpaId);
        return count != null && count > 0;
    }

    private Mpa mapRowForMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME_MPA"))
                .build();
    }
}
