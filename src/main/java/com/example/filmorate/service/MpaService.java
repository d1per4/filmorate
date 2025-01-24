package com.example.filmorate.service;

import com.example.filmorate.exception.NotFoundException;
import com.example.filmorate.model.Mpa;
import com.example.filmorate.storage.MpaStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaDbStorage;

    public List<Mpa> getMpa() {
        return mpaDbStorage.getMpa();
    }

    public Mpa getMpaById(int mpaId) {
        return mpaDbStorage.getMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("MPA не найден."));
    }


}
