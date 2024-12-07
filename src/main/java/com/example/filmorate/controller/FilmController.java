package com.example.filmorate.controller;

import com.example.filmorate.exception.LocalDateException;
import com.example.filmorate.model.Film;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    private final List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> findAll(){
        return films;
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film){
        if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            throw new LocalDateException("Дата не может быть раньше 28 декабря 1895 года");
        }
        films.add(film);
        return film;
    }


}
