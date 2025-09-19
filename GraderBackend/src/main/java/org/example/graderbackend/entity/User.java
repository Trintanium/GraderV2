/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.EmailVerification
 *  com.example.graderbackend.entity.Role
 *  com.example.graderbackend.entity.Submission
 *  com.example.graderbackend.entity.User
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

import com.example.graderbackend.entity.EmailVerification;
import com.example.graderbackend.entity.Role;
import com.example.graderbackend.entity.Submission;
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
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_seq")
    @SequenceGenerator(name="user_seq", sequenceName="user_seq", allocationSize=1)
    private Long id;
    @Column(unique=true)
    private String username;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    @Enumerated(value=EnumType.STRING)
    private Role role = Role.USER;
    @Column(nullable=false)
    private String profilePicture = "";
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @Column(nullable=false)
    private boolean emailVerified = false;
    @OneToMany(mappedBy="user", cascade={CascadeType.ALL}, orphanRemoval=true)
    private List<EmailVerification> emailVerification;
    @OneToMany(mappedBy="user", cascade={CascadeType.ALL}, orphanRemoval=true)
    private List<Submission> submissions;

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setUsername(String username) {
        this.username = username;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setPassword(String password) {
        this.password = password;
    }

    @Generated
    public void setRole(Role role) {
        this.role = role;
    }

    @Generated
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Generated
    public void setEmailVerification(List<EmailVerification> emailVerification) {
        this.emailVerification = emailVerification;
    }

    @Generated
    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getPassword() {
        return this.password;
    }

    @Generated
    public Role getRole() {
        return this.role;
    }

    @Generated
    public String getProfilePicture() {
        return this.profilePicture;
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
    public boolean isEmailVerified() {
        return this.emailVerified;
    }

    @Generated
    public List<EmailVerification> getEmailVerification() {
        return this.emailVerification;
    }

    @Generated
    public List<Submission> getSubmissions() {
        return this.submissions;
    }
}

