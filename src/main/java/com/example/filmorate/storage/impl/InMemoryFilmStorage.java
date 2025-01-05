package com.example.filmorate.storage.impl;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import com.example.filmorate.storage.FilmStorage;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int uniqueId = 1;
    private final Map<Integer, Film> films = new HashMap<>();


    @Override
    public Film create(Film film) {
        film.setId(getUniqueId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film updateFilm = films.get(film.getId());

        if (updateFilm == null) {
            throw new NotFoundException("Фильм не найден");
        }

        films.put(film.getId(), updateFilm);
        return updateFilm;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Collection<Film> findAllPopular() {
        return List.of();
    }

    private int getUniqueId() {
        return uniqueId++;
    }
}
