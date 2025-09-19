/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.Problem
 *  com.example.graderbackend.entity.TestCase
 *  com.example.graderbackend.entity.Type
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
import com.example.graderbackend.entity.Type;
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
@Table(name="testcase")
public class TestCase {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="testcase_sql")
    @SequenceGenerator(name="testcase_sql", sequenceName="testcase_sql", allocationSize=1)
    @Column(nullable=false)
    private Long id;
    @ManyToOne
    @JoinColumn(name="problem_id", nullable=false)
    private Problem problem;
    @Column(nullable=false)
    private String input;
    @Column(nullable=false)
    private String output;
    @Enumerated(value=EnumType.STRING)
    private Type type = Type.PUBLIC;
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
    public String getInput() {
        return this.input;
    }

    @Generated
    public String getOutput() {
        return this.output;
    }

    @Generated
    public Type getType() {
        return this.type;
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
    public void setInput(String input) {
        this.input = input;
    }

    @Generated
    public void setOutput(String output) {
        this.output = output;
    }

    @Generated
    public void setType(Type type) {
        this.type = type;
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

