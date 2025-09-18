package com.example.graderworkerapplication.dto;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSendMessage {
    private Long submissionId;
    private SubmissionDto submissionDto;        // ใช้ DTO แทน code + language
    private List<TestCaseDto> testCasesDtoList; // list ของ test case
}