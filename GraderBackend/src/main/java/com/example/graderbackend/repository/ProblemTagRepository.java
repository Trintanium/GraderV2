/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.ProblemTag
 *  com.example.graderbackend.repository.ProblemTagRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.example.graderbackend.repository;

import com.example.graderbackend.entity.ProblemTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemTagRepository
extends JpaRepository<ProblemTag, Long> {
    public List<ProblemTag> findByProblemId(Long var1);

    public void deleteByProblemIdAndTagId(Long var1, Long var2);
}

