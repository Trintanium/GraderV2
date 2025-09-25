/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.EmailVerificationDto
 *  lombok.Generated
 */
package com.example.graderbackend.dto.entity;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class EmailVerificationDto {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiry;
    private boolean used;


}

