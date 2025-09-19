/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.Problem
 *  com.example.graderbackend.entity.Status
 *  com.example.graderbackend.entity.Submission
 *  com.example.graderbackend.entity.User
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.EnumType
 *  jakarta.persistence.Enumerated
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.JoinColumn
 *  jakarta.persistence.ManyToOne
 *  jakarta.persistence.SequenceGenerator
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.example.graderbackend.entity;

import com.example.graderbackend.entity.Problem;
import com.example.graderbackend.entity.Status;
import com.example.graderbackend.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(name="submission")
public class Submission {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="submission_seq")
    @SequenceGenerator(name="submission_seq", sequenceName="submission_seq", allocationSize=1)
    private Long id;
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @ManyToOne(optional=false)
    @JoinColumn(name="problem_id", nullable=false)
    private Problem problem;
    @Column(nullable=false)
    private String language;
    @Column(nullable=false, columnDefinition="TEXT")
    private String code;
    @Column(nullable=false)
    private Float score = Float.valueOf(0.0f);
    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    private Status status = Status.PENDING;
    @Column(nullable=false)
    private LocalDateTime submittedAt = LocalDateTime.now();
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public User getUser() {
        return this.user;
    }

    @Generated
    public Problem getProblem() {
        return this.problem;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public Float getScore() {
        return this.score;
    }

    @Generated
    public Status getStatus() {
        return this.status;
    }

    @Generated
    public LocalDateTime getSubmittedAt() {
        return this.submittedAt;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setUser(User user) {
        this.user = user;
    }

    @Generated
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setScore(Float score) {
        this.score = score;
    }

    @Generated
    public void setStatus(Status status) {
        this.status = status;
    }

    @Generated
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

