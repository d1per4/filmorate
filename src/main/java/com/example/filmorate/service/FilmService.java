package com.example.filmorate.service;

import com.example.filmorate.exception.LocalDateException;
import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import com.example.filmorate.model.User;
import com.example.filmorate.storage.FilmStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

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
        Film film = findById(filmId);
        User user = userService.findById(userId);

        filmStorage.addLike(film.getId(), user.getId());
    }

    public void removeLike(int filmId, int userId){
        Film film = findById(filmId);
        User user = userService.findById(userId);

        filmStorage.removeLike(film.getId(), user.getId());
    }


    public List<Film> getPopularFilms(Integer count) {

        List<Film> films = new ArrayList<>(findAll().stream().toList());

        films.sort(Comparator.comparing((Film film) -> film.getLikes().size()).reversed());

        return films.stream()
                .limit(Objects.requireNonNullElse(count, 10))
                .collect(Collectors.toList());
    }
}
