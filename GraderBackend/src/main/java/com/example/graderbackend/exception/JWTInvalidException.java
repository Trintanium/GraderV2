/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.exceptions.JWTException
 *  com.example.graderbackend.exceptions.JWTInvalidException
 */
package com.example.graderbackend.exception;

public class JWTInvalidException
extends JWTException {
    public JWTInvalidException(String message) {
        super(message);
    }

    public JWTInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}

