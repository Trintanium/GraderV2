/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.AuthResponse
 *  com.example.graderbackend.dto.RegisterRequest
 *  com.example.graderbackend.dto.TokenResponse
 *  com.example.graderbackend.entity.User
 *  com.example.graderbackend.service.AuthService
 *  jakarta.servlet.http.HttpServletResponse
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.AuthResponse;
import com.example.graderbackend.dto.RegisterRequest;
import com.example.graderbackend.dto.TokenResponse;
import com.example.graderbackend.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public void registerUser(String var1, String var2, String var3, String var4);

    public AuthResponse loginAndGetUser(RegisterRequest var1, HttpServletResponse var2);

    public TokenResponse createTokenResponse(String var1);

    public TokenResponse refreshAccessToken(String var1);

    public void forgotPasswordVerification(String var1);

    public void sendEmailVerification(User var1);
}

