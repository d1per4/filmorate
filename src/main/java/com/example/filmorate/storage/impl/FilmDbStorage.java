package com.example.filmorate.storage.impl;

import com.example.filmorate.exception.InvalidGenreException;
import com.example.filmorate.exception.InvalidMpaException;
import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import com.example.filmorate.model.Genre;
import com.example.filmorate.model.Mpa;
import com.example.filmorate.storage.FilmStorage;
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

    @Override
    public Film create(Film film) {

        Integer mpaId = film.getMpa().getId();

        if (!mpaExists(mpaId)) {
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

        try {
            Set<Genre> collect = film.getGenres().stream()
                    .map(genre -> getGenreById(genre.getId())
                            .orElseThrow(() ->
                                    new InvalidGenreException("Жанр " + genre.getId() + " не существует")))
                    .collect(Collectors.toSet());

            Set<Genre> sortedGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            sortedGenres.addAll(collect);
            film.setGenres(sortedGenres);

            addGenresToFilm(id, sortedGenres);
        } catch (NullPointerException e) {
            System.out.println("Не найдены жанры");
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


    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(getMpaById(rs.getInt("MPA_ID"))
                        .orElseThrow(() -> new InvalidMpaException("MPA не существует.")))
                .build();

        Set<Genre> sortedGenres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        sortedGenres.addAll(getGenresForFilm(rs.getInt("ID")));
        film.setGenres(sortedGenres);
        return film;
    }

    private Mpa mapRowForMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME_MPA"))
                .build();
    }

    private Genre mapRowForGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME_GENRE"))
                .build();
    }

    private boolean mpaExists(Integer mpaId) {
        String sql = "SELECT COUNT(*) FROM MPA WHERE ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, mpaId);
        return count != null && count > 0;
    }


    private Set<Genre> getGenresForFilm(int filmId){
        String sql = """
                SELECT G.ID, G.NAME_GENRE FROM GENRES G
                JOIN FILM_GENRES FG ON G.ID = FG.GENRE_ID
                WHERE FG.FILM_ID = ?
                ORDER BY G.ID DESC
                """;
        return new HashSet<>(jdbcTemplate.query(sql, this::mapRowForGenre, filmId));
    }

    private void addGenresToFilm(int filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return; // Если жанры отсутствуют, ничего не делаем
        }

        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        // Подготовка данных для batchUpdate
        List<Object[]> batchArgs = genres.stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        // Выполняем batchUpdate
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    


}
