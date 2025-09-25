/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.ProblemTag
 *  com.example.graderbackend.entity.Tag
 *  jakarta.persistence.CascadeType
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.OneToMany
 *  jakarta.persistence.SequenceGenerator
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.example.graderbackend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="tag")
public class Tag {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="tag_seq")
    @SequenceGenerator(name="tag_seq", sequenceName="tag_seq", allocationSize=1)
    @Column(nullable=false)
    private Long id;
    @Column(nullable=false, unique=true)
    private String name;
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy="tag", cascade={CascadeType.ALL}, orphanRemoval=true)
    private List<ProblemTag> problemTags;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getName() {
        return this.name;
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
    public List<ProblemTag> getProblemTags() {
        return this.problemTags;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
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
    public void setProblemTags(List<ProblemTag> problemTags) {
        this.problemTags = problemTags;
    }
}

