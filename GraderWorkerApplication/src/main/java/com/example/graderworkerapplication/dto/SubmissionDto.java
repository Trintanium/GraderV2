package com.example.graderworkerapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDto {
    private Long id;
    private String code;       // ต้องมี field นี้
    private String language;   // ต้องมี field นี้
    private Long userId;
    private Long problemId;
}
