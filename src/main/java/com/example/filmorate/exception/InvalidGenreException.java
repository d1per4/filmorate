package com.example.filmorate.exception;

public class InvalidGenreException extends RuntimeException {
    public InvalidGenreException(String message){
        super(message);
    }
}
