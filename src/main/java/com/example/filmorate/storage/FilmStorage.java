package com.example.filmorate.storage;

import com.example.filmorate.model.Film;
import com.example.filmorate.model.Genre;
import com.example.filmorate.model.Mpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    int update(Film film);

    Collection<Film> findAll();

    Optional<Film> findById(int filmId);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopularFilms(Integer count);

    List<Mpa> getMpa();

    Optional<Mpa> getMpaById(int mpaId);

    List<Genre> getGenres();

    Optional<Genre> getGenreById(int genreId);
}
