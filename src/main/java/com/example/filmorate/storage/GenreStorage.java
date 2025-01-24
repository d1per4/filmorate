package com.example.filmorate.storage;

import com.example.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    List<Genre> getGenres();

    Optional<Genre> getGenreById(int genreId);

    Set<Genre> getGenresForFilm(int genreId);

    void addGenresToFilm(int filmId, Set<Genre> genres);
}
