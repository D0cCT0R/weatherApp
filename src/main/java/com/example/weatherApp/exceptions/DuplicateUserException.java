package com.example.weatherApp.exceptions;

import lombok.Getter;

@Getter
public class DuplicateUserException extends Exception{
    public DuplicateUserException(String errorMessage) {
        super(errorMessage);
    }
}
