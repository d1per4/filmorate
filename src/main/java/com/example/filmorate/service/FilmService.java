package com.example.filmorate.service;

import com.example.filmorate.exception.InvalidMpaException;
import com.example.filmorate.exception.LocalDateException;
import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import com.example.filmorate.model.Genre;
import com.example.filmorate.model.Mpa;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.FilmStorage;
import com.example.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmDbStorage;
    private final UserStorage userDbStorage;

    public Film create(Film film){
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new LocalDateException("Дата не может быть раньше 28 декабря 1895 года");
        }


        return filmDbStorage.create(film);
    }

    public Collection<Film> findAll(){
        return filmDbStorage.findAll();
    }

    public Film update(Film film){
        int updatedRows = filmDbStorage.update(film);

        if (updatedRows == 0) {
            throw new NotFoundException("Фильм не найден");
        }

        return film;
    }

    public Film findById(int filmId){
        return filmDbStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public void addLike(int filmId, int userId){
        Film film = findById(filmId);
        User user = userDbStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        filmDbStorage.addLike(film.getId(), user.getId());
    }

    public void removeLike(int filmId, int userId){
        Film film = findById(filmId);
        User user = userDbStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        filmDbStorage.removeLike(film.getId(), user.getId());
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmDbStorage.getPopularFilms(count);
    }

    public List<Mpa> getMpa() {
        return filmDbStorage.getMpa();
    }

    public Mpa getMpaById(int mpaId) {
        return filmDbStorage.getMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("MPA не найден."));
    }

    public List<Genre> getGenres(){
        return filmDbStorage.getGenres();
    }

    public Genre getGenresById(int genreId) {
        return filmDbStorage.getGenreById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}
