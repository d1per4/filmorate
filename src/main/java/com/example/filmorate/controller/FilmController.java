package com.example.filmorate.controller;

import com.example.filmorate.model.Film;
import com.example.filmorate.model.Genre;
import com.example.filmorate.model.Mpa;
import com.example.filmorate.service.FilmService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("/films/{filmId}/like/{id}")
    public void addLike(@PathVariable int filmId,
                        @PathVariable int id){
        filmService.addLike(filmId, id);
    }

    @DeleteMapping("/films/{filmId}/like/{id}")
    public void removeLike(@PathVariable int filmId,
                           @PathVariable int id){
        filmService.removeLike(filmId, id);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count){
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpa(){
        return filmService.getMpa();
    }

    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpaById(@PathVariable int mpaId){
        return filmService.getMpaById(mpaId);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres(){
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenresById(@PathVariable int id){
        return filmService.getGenresById(id);
    }

    @GetMapping("/films/{id}")
    public Film getFilmWithGenre(@PathVariable int id){
        return filmService.getFilmWithGenre(id);
    }




}
