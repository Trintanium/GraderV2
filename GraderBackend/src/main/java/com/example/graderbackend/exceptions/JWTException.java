/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.exceptions.JWTException
 */
package com.example.graderbackend.exceptions;

public class JWTException
extends RuntimeException {
    public JWTException(String message) {
        super(message);
    }

    public JWTException(String message, Throwable cause) {
        super(message, cause);
    }
}

