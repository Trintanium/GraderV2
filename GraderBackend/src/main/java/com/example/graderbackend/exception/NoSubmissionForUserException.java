package com.example.graderbackend.exception;

public class NoSubmissionForUserException extends RuntimeException {
    public NoSubmissionForUserException(Long userId) {
        super("No submissions found for user " + userId);
    }
}