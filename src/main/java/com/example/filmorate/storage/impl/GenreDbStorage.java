package com.example.filmorate.storage.impl;

import com.example.filmorate.model.Genre;
import com.example.filmorate.storage.GenreStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, this::mapRowForGenre);
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        String sql = "SELECT * FROM GENRES WHERE ID = ?";
        return jdbcTemplate.query(sql, this::mapRowForGenre, genreId)
                .stream()
                .findFirst();
    }

    @Override
    public Set<Genre> getGenresForFilm(int filmId) {
        String sql = """
                SELECT G.ID, G.NAME_GENRE FROM GENRES G
                JOIN FILM_GENRES FG ON G.ID = FG.GENRE_ID
                WHERE FG.FILM_ID = ?
                ORDER BY G.ID DESC
                """;
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowForGenre, filmId));
    }

    @Override
    public void addGenresToFilm(int filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        List<Object[]> batchArgs = genres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    private Genre mapRowForGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME_GENRE"))
                .build();
    }
}
