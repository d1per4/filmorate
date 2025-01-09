package com.example.filmorate.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }

}
