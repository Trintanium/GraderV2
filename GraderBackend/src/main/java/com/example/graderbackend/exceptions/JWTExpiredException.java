/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.exceptions.JWTException
 *  com.example.graderbackend.exceptions.JWTExpiredException
 */
package com.example.graderbackend.exceptions;

public class JWTExpiredException
extends JWTException {
    public JWTExpiredException(String message) {
        super(message);
    }
}

