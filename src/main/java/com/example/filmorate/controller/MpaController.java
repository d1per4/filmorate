package com.example.filmorate.controller;

import com.example.filmorate.model.Mpa;
import com.example.filmorate.service.MpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/mpa")
    public List<Mpa> getMpa(){
        return mpaService.getMpa();
    }

    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpaById(@PathVariable int mpaId){
        return mpaService.getMpaById(mpaId);
    }
}
