package com.example.weatherApp.exceptions;

public class AuthenticationFailedException extends Exception{
    public AuthenticationFailedException(String message) {
        super(message);
    }
}

