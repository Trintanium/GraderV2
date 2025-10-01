package com.example.graderbackend.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("Invalid or expired token: " + token);
    }
}
