/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.User
 *  com.example.graderbackend.repository.UserRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.example.graderbackend.repository;

import com.example.graderbackend.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String var1);

    public Optional<User> findByUsername(String var1);
}

