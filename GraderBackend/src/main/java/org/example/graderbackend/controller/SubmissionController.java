/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.controller.SubmissionController
 *  com.example.graderbackend.dto.entity.SubmissionDto
 *  com.example.graderbackend.service.SubmissionService
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.SubmissionDto;
import com.example.graderbackend.service.SubmissionService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/submissions"})
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<SubmissionDto> createSubmission(@RequestBody SubmissionDto submission) {
        return ResponseEntity.ok(this.submissionService.createSubmission(submission));
    }

    @GetMapping(value={"/all"})
    public ResponseEntity<List<SubmissionDto>> getAllSubmissions() {
        return ResponseEntity.ok(this.submissionService.getAllSubmissions());
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(this.submissionService.getSubmissionById(id));
    }

    @GetMapping(value={"/my/latest"})
    public ResponseEntity<SubmissionDto> getMyLatestSubmission() {
        return ResponseEntity.ok(this.submissionService.getMyLatestSubmission());
    }

    @GetMapping(value={"/my"})
    public ResponseEntity<List<SubmissionDto>> getMySubmissions() {
        return ResponseEntity.ok(this.submissionService.getMySubmissions());
    }
}

