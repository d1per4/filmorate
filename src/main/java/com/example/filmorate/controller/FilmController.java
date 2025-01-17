package com.example.filmorate.controller;

import com.example.filmorate.model.Film;
import com.example.filmorate.model.Mpa;
import com.example.filmorate.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{filmId}/like/{id}")
    public void addLike(@PathVariable int filmId,
                        @PathVariable int id){
        filmService.addLike(filmId, id);
    }

    @DeleteMapping("/{filmId}/like/{id}")
    public void removeLike(@PathVariable int filmId,
                           @PathVariable int id){
        filmService.removeLike(filmId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count){
        return filmService.getPopularFilms(count);
    }

    @PostMapping("/mpa")
    public void addMpa(String name){
        filmService.addMpa(name);
    }


}
