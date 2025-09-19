/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.SubmissionDto
 *  com.example.graderbackend.entity.Status
 *  lombok.Generated
 */
package com.example.graderbackend.dto.entity;

import com.example.graderbackend.entity.Status;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDto {
    Long id;
    Long userId;
    Long problemId;
    String code;
    Float score;
    String language;
    Status status;
    LocalDateTime submittedAt;
}

