/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.exceptions.GlobalExceptionHandler
 *  com.example.graderbackend.exceptions.JWTExpiredException
 *  com.example.graderbackend.exceptions.JWTInvalidException
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.ControllerAdvice
 *  org.springframework.web.bind.annotation.ExceptionHandler
 */
package com.example.graderbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value={JWTInvalidException.class, JWTExpiredException.class})
    public ResponseEntity<String> handleJWTExceptions(RuntimeException ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<String> handleAll(Exception ex) {
        return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}

