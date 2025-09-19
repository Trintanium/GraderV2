/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.UserDto
 *  com.example.graderbackend.entity.EmailVerification
 *  com.example.graderbackend.entity.User
 *  com.example.graderbackend.service.EmailVerificationService
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.entity.UserDto;
import com.example.graderbackend.entity.EmailVerification;
import com.example.graderbackend.entity.User;
import java.util.Optional;

public interface EmailVerificationService {
    public EmailVerification createEmailVerification(User var1);

    public Optional<UserDto> verifyEmailAndReturnUser(String var1);

    public boolean resetPassword(String var1, String var2, String var3);

    public Optional<EmailVerification> getValidEmailToken(String var1);

    public void sendVerificationEmail(EmailVerification var1);

    public void sendForgotPasswordEmail(EmailVerification var1);

    public void sendEmail(String var1, String var2, String var3, String var4, String var5);
}

