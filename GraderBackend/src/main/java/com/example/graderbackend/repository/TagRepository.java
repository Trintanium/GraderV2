/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.entity.Tag
 *  com.example.graderbackend.repository.TagRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.example.graderbackend.repository;

import com.example.graderbackend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository
extends JpaRepository<Tag, Long> {
}

