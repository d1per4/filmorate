package com.example.filmorate.controller;

import com.example.filmorate.model.Genre;
import com.example.filmorate.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> getGenres(){
        return genreService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenresById(@PathVariable int id){
        return genreService.getGenresById(id);
    }

}
