package com.example.graderbackend.exception;

public class SubmissionSendException extends RuntimeException {
    public SubmissionSendException(String message, Throwable cause) {
        super(message, cause);
    }
}