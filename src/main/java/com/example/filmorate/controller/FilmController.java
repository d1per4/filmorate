package com.example.filmorate.controller;

import com.example.filmorate.exception.LocalDateException;
import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int uniqueId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getUniqueId());
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new LocalDateException("Дата не может быть раньше 28 декабря 1895 года");
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/{id}")
    public Film update(@PathVariable Integer id, @Valid @RequestBody Film film) {
        Film updateFilm = films.get(id);

        if (updateFilm == null) {
            throw new NotFoundException("Фильм не найден");
        }

        if (film.getName() != null && !film.getName().isBlank()) {
            updateFilm.setName(film.getName());
        }
        if (film.getDescription() != null) {
            updateFilm.setDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null) {
            updateFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() != null) {
            updateFilm.setDuration(film.getDuration());
        }

        films.put(id, updateFilm);

        return updateFilm;
    }

    private int getUniqueId() {
        return uniqueId++;
    }


}
