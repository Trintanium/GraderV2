/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.EmailVerification
 *  com.example.graderbackend.repository.EmailVerificationRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.example.graderbackend.repository;

import com.example.graderbackend.entity.EmailVerification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository
extends JpaRepository<EmailVerification, Long> {
    public Optional<EmailVerification> findByToken(String var1);
}

