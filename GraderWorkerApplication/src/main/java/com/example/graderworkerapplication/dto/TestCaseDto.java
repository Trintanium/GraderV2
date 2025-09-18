package com.example.graderworkerapplication.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseDto {
    private Long id;
    private String input;
    private String output;
}