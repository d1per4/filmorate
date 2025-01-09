package com.example.filmorate.controller;

import com.example.filmorate.exception.LocalDateException;
import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Film;
import com.example.filmorate.service.FilmService;
import com.example.filmorate.storage.FilmStorage;
import com.example.filmorate.storage.impl.InMemoryFilmStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/{film_id}/like/{id}")
    public void addLike(@PathVariable int film_id,
                        @PathVariable int id){
        filmService.addLike(film_id, id);
    }

    @DeleteMapping("/{film_id}/like/{id}")
    public void removeLike(@PathVariable int film_id,
                           @PathVariable int id){
        filmService.removeLike(film_id, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count){
        return filmService.getPopularFilms(count);
    }

}
