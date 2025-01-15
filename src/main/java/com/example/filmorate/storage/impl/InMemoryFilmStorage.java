package com.example.filmorate.storage.impl;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import com.example.filmorate.storage.FilmStorage;
import org.springframework.stereotype.Component;

import java.util.*;

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
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Optional<Film> findById(int filmId){
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public void addLike(int filmId, int userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return findAll().stream()
                .sorted(Comparator.comparing((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }

    private int getUniqueId() {
        return uniqueId++;
    }


}
