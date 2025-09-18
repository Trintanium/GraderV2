package com.example.graderworkerapplication.dto;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResultMessage {
    private Long submissionId;
    private String status;
    private Float score;
    private List<TestCaseResult> results;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCaseResult {
        private Long testCaseId;
        private boolean passed;
        private String actualOutput;
    }
}
