/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.Difficulty
 *  com.example.graderbackend.entity.Problem
 *  com.example.graderbackend.entity.ProblemTag
 *  com.example.graderbackend.entity.Submission
 *  com.example.graderbackend.entity.TestCase
 *  jakarta.persistence.CascadeType
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.EnumType
 *  jakarta.persistence.Enumerated
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.OneToMany
 *  jakarta.persistence.SequenceGenerator
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.example.graderbackend.entity;

import com.example.graderbackend.entity.Difficulty;
import com.example.graderbackend.entity.ProblemTag;
import com.example.graderbackend.entity.Submission;
import com.example.graderbackend.entity.TestCase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Generated;

@Entity
@Table(name="problem")
public class Problem {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="problem_seq")
    @SequenceGenerator(name="problem_seq", sequenceName="problem_seq", allocationSize=1)
    private Long id;
    @Column(nullable=false)
    private String title;
    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    private Difficulty difficulty;
    @Column(nullable=false)
    private String pdf;
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy="problem", cascade={CascadeType.ALL}, orphanRemoval=true)
    private List<Submission> submissions;
    @OneToMany(mappedBy="problem", cascade={CascadeType.ALL}, orphanRemoval=true)
    private List<TestCase> testCases;
    @OneToMany(mappedBy="problem", cascade={CascadeType.ALL}, orphanRemoval=true)
    private List<ProblemTag> problemTags;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    @Generated
    public String getPdf() {
        return this.pdf;
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
    public List<Submission> getSubmissions() {
        return this.submissions;
    }

    @Generated
    public List<TestCase> getTestCases() {
        return this.testCases;
    }

    @Generated
    public List<ProblemTag> getProblemTags() {
        return this.problemTags;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Generated
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    @Generated
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    @Generated
    public void setProblemTags(List<ProblemTag> problemTags) {
        this.problemTags = problemTags;
    }
}

