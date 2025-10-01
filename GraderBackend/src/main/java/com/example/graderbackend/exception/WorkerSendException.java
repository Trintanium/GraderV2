package com.example.graderbackend.exception;

public class WorkerSendException extends RuntimeException {
    public WorkerSendException(String message, Throwable cause) {
        super(message, cause);
    }
}