package com.example.graderworkerapplication.dto;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSendMessage {
    private Long submissionId;
    private SubmissionDto submissionDto;
    private List<TestCaseDto> testCasesDtoList;
}