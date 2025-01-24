package com.example.filmorate.storage.impl;

import com.example.filmorate.exception.InvalidGenreException;
import com.example.filmorate.exception.InvalidMpaException;
import com.example.filmorate.model.Film;
import com.example.filmorate.model.Genre;
import com.example.filmorate.storage.FilmStorage;
import com.example.filmorate.storage.GenreStorage;
import com.example.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreDbStorage;
    private final MpaStorage mpaDbStorage;

    @Override
    public Film create(Film film) {

        Integer mpaId = film.getMpa().getId();

        if (!mpaDbStorage.mpaExists(mpaId)) {
            throw new InvalidMpaException("MPA ID " + mpaId + " не существует.");
        }

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");

        Map<String, Object> params = Map.of(
                "NAME", film.getName(),
                "DESCRIPTION", film.getDescription(),
                "RELEASE_DATE", film.getReleaseDate(),
                "DURATION", film.getDuration(),
                "MPA_ID", film.getMpa().getId()
        );

        int id = insert.executeAndReturnKey(params).intValue();
        film.setId(id);

        if(film.getGenres() != null) {
            Set<Genre> collect = film.getGenres().stream()
                    .map(genre -> genreDbStorage.getGenreById(genre.getId())
                            .orElseThrow(() ->
                                    new InvalidGenreException("Жанр " + genre.getId() + " не существует")))
                    .collect(Collectors.toSet());

            Set<Genre> sortedGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            sortedGenres.addAll(collect);
            film.setGenres(sortedGenres);

            genreDbStorage.addGenresToFilm(id, sortedGenres);
        }

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
        String sql = """
                SELECT F."ID", "NAME", "DESCRIPTION", "RELEASE_DATE", "DURATION", "MPA_ID", M.NAME_MPA FROM FILMS F
                JOIN MPA M ON F.MPA_ID = M.ID
                WHERE F.ID = ?
                """;
        return jdbcTemplate.query(sql, this::mapRow, filmId)
                .stream()
                .findFirst();
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = """
                SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.MPA_ID, COUNT(L.USER_ID) FROM FILMS F
                LEFT JOIN LIKES L ON F."ID" = L."FILM_ID"
                GROUP BY F.ID
                ORDER BY COUNT(L.USER_ID) DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, this::mapRow, count);
    }


    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(mpaDbStorage.getMpaById(rs.getInt("MPA_ID"))
                        .orElseThrow(() -> new InvalidMpaException("MPA не существует.")))
                .build();

        Set<Genre> sortedGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        sortedGenres.addAll(genreDbStorage.getGenresForFilm(rs.getInt("ID")));
        film.setGenres(sortedGenres);
        return film;
    }








}
