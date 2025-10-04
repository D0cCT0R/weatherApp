package com.example.weatherApp.exceptions;


public class DatabaseIsNotAvailableException extends Exception{
    public DatabaseIsNotAvailableException(String message) {
        super(message);
    }
}

