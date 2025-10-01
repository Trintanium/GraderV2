package com.example.graderbackend.exception;


public class InvalidFileFormatException extends RuntimeException {
    public InvalidFileFormatException(String filename) {
        super("Invalid file format for: " + filename + ". File must be PDF.");
    }
}
