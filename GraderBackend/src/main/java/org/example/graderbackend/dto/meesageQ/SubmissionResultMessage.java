/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.meesageQ.SubmissionResultMessage
 *  com.example.graderbackend.dto.meesageQ.SubmissionResultMessage$TestCaseResult
 *  lombok.Generated
 */
package com.example.graderbackend.dto.meesageQ;

import com.example.graderbackend.dto.meesageQ.SubmissionResultMessage;
import java.util.List;

import lombok.*;

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

