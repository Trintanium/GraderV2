/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.UserDto
 *  com.example.graderbackend.entity.Role
 *  lombok.Generated
 */
package com.example.graderbackend.dto.entity;

import com.example.graderbackend.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    String username;
    String email;
    Role role;
    String profilePicture;
    boolean emailVerified;
}

