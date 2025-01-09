package com.example.filmorate.service;

import com.example.filmorate.exception.LocalDateException;
import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.FilmStorage;
import com.example.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public Film create(Film film){
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new LocalDateException("Дата не может быть раньше 28 декабря 1895 года");
        }
        return filmStorage.create(film);
    }

    public Collection<Film> findAll(){
        return filmStorage.findAll();
    }

    public Film update(Film film){
        findById(film.getId());
        return filmStorage.update(film);
    }

    public Film findById(int filmId){
        return filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public void addLike(int filmId, int userId){
        if(filmStorage.findById(filmId).isEmpty()){
            throw new NotFoundException("Фильм не найден");
        }

        if(userStorage.findById(userId).isEmpty()){
            throw new NotFoundException("Пользователь не найден");
        }

        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId){
        if(filmStorage.findById(filmId).isEmpty()){
            throw new NotFoundException("Фильм не найден");
        }

        if(userStorage.findById(userId).isEmpty()){
            throw new NotFoundException("Пользователь не найден");
        }

        filmStorage.removeLike(filmId, userId);
    }



}
