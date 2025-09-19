/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.TestCase
 *  com.example.graderbackend.repository.TestCaseRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.example.graderbackend.repository;

import com.example.graderbackend.entity.TestCase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository
extends JpaRepository<TestCase, Long> {
    public List<TestCase> findByProblem_Id(Long var1);
}

