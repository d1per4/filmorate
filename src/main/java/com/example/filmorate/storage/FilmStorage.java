package com.example.filmorate.storage;

import com.example.filmorate.model.Film;

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


}
