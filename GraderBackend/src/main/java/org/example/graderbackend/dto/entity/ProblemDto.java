/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.ProblemDto
 *  com.example.graderbackend.entity.Difficulty
 *  lombok.Generated
 */
package com.example.graderbackend.dto.entity;

import com.example.graderbackend.entity.Difficulty;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {
    private Long id;
    private String title;
    private Difficulty difficulty;
    private String pdf;
    private String pdfUrl;
}

