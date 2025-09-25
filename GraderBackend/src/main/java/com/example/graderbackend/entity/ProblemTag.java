/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.Problem
 *  com.example.graderbackend.entity.ProblemTag
 *  com.example.graderbackend.entity.Tag
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.JoinColumn
 *  jakarta.persistence.ManyToOne
 *  jakarta.persistence.SequenceGenerator
 *  jakarta.persistence.Table
 *  jakarta.persistence.UniqueConstraint
 *  lombok.Generated
 */
package com.example.graderbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Generated;

@Entity
@Table(name="problem_tag", uniqueConstraints={@UniqueConstraint(columnNames={"problem_id", "tag_id"})})
public class ProblemTag {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="problem_tag_seq")
    @SequenceGenerator(name="problem_tag_seq", sequenceName="problem_tag_seq", allocationSize=1)
    @Column(nullable=false)
    private Long id;
    @ManyToOne
    @JoinColumn(name="problem_id", nullable=false)
    private Problem problem;
    @ManyToOne
    @JoinColumn(name="tag_id", nullable=false)
    private Tag tag;
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Problem getProblem() {
        return this.problem;
    }

    @Generated
    public Tag getTag() {
        return this.tag;
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
    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Generated
    public void setTag(Tag tag) {
        this.tag = tag;
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

