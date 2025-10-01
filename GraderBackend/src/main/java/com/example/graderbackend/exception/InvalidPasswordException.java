package com.example.graderbackend.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("New password must not be the same as the old password");
    }
}
