package com.example.filmorate.storage;

import com.example.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    List<Mpa> getMpa();

    Optional<Mpa> getMpaById(int mpaId);

    boolean mpaExists(Integer mpaId);
}
