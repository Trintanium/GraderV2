/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.EmailVerification
 *  com.example.graderbackend.entity.User
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
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

import com.example.graderbackend.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;

@Table(name="emailverification")
@Entity
public class EmailVerification {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="emailverification_seq")
    @SequenceGenerator(name="emailverification_seq", sequenceName="emailverification_seq", allocationSize=1)
    @Column(nullable=false)
    private Long id;
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;
    @Column(nullable=false, unique=true)
    private String token;
    @Column(nullable=false)
    private LocalDateTime expiry;
    @Column(nullable=false)
    private boolean used = false;
    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public User getUser() {
        return this.user;
    }

    @Generated
    public String getToken() {
        return this.token;
    }

    @Generated
    public LocalDateTime getExpiry() {
        return this.expiry;
    }

    @Generated
    public boolean isUsed() {
        return this.used;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
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
    public void setToken(String token) {
        this.token = token;
    }

    @Generated
    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    @Generated
    public void setUsed(boolean used) {
        this.used = used;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

