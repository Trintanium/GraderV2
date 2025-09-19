/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.Submission
 *  com.example.graderbackend.repository.SubmissionRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.example.graderbackend.repository;

import com.example.graderbackend.entity.Submission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository
extends JpaRepository<Submission, Long> {
    public Optional<Submission> findTopByUser_IdOrderBySubmittedAtDesc(Long var1);

    public List<Submission> findByUser_Id(Long var1);
}

