package com.example.graderbackend.exception;

public class AuthenticatedUserNotFoundException extends RuntimeException {
    public AuthenticatedUserNotFoundException() {
        super("User not Authenticated");
    }
}
