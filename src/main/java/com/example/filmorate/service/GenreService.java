package com.example.filmorate.service;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Genre;
import com.example.filmorate.storage.GenreStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreDbStorage;

    public List<Genre> getGenres(){
        return genreDbStorage.getGenres();
    }

    public Genre getGenresById(int genreId) {
        return genreDbStorage.getGenreById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }

}
