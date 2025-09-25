/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.TestCaseDto
 *  lombok.Generated
 */
package com.example.graderbackend.dto.entity;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseDto {
    private Long id;
    private Long problemId;
    private String input;
    private String output;
    private String type;
}

