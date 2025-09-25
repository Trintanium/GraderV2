/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.AuthResponse
 *  com.example.graderbackend.dto.entity.UserDto
 *  lombok.Generated
 */
package com.example.graderbackend.dto;

import com.example.graderbackend.dto.entity.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private UserDto user;
}

