/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.SubmissionDto
 *  com.example.graderbackend.dto.meesageQ.SubmissionResultMessage
 *  com.example.graderbackend.service.SubmissionService
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.entity.SubmissionDto;
import com.example.graderbackend.dto.meesageQ.SubmissionResultMessage;
import java.util.List;

public interface SubmissionService {
    public SubmissionDto createSubmission(SubmissionDto var1);

    public SubmissionDto getSubmissionById(Long var1);

    public List<SubmissionDto> getAllSubmissions();

    public SubmissionDto updateSubmission(Long var1, SubmissionDto var2);

    public void deleteSubmission(Long var1);

    public void updateSubmissionResult(SubmissionResultMessage var1);

    public SubmissionDto getMyLatestSubmission();

    public List<SubmissionDto> getMySubmissions();
}

