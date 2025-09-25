/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.ProblemTagDto
 *  lombok.Generated
 */
package com.example.graderbackend.dto.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemTagDto {
    Long id;
    Long problemId;
    Long tagId;
}

