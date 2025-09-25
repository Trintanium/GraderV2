/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.Problem
 *  com.example.graderbackend.repository.ProblemRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.example.graderbackend.repository;

import com.example.graderbackend.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository
extends JpaRepository<Problem, Long> {
}

