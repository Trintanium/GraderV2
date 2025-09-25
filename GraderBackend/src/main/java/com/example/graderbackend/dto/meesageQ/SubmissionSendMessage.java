/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.SubmissionDto
 *  com.example.graderbackend.dto.entity.TestCaseDto
 *  com.example.graderbackend.dto.meesageQ.SubmissionSendMessage
 *  lombok.Generated
 */
package com.example.graderbackend.dto.meesageQ;

import com.example.graderbackend.dto.entity.SubmissionDto;
import com.example.graderbackend.dto.entity.TestCaseDto;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionSendMessage {
    private Long submissionId;
    private SubmissionDto submissionDto;
    private List<TestCaseDto> testCasesDtoList;

   }

