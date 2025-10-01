package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.SubmissionDto;
import com.example.graderbackend.service.SubmissionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // ------------------- CREATE SUBMISSION -------------------
    @PostMapping
    public ResponseEntity<SubmissionDto> createSubmission(@RequestBody SubmissionDto submission) {
        SubmissionDto created = submissionService.createSubmission(submission);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    // ------------------- GET ALL SUBMISSIONS -------------------
    @GetMapping("/all")
    public ResponseEntity<List<SubmissionDto>> getAllSubmissions() {
        List<SubmissionDto> submissions = submissionService.getAllSubmissions();
        return ResponseEntity.ok(submissions); // 200 OK
    }

    // ------------------- GET SUBMISSION BY ID -------------------
    @GetMapping("/{id}")
    public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable Long id) {
        SubmissionDto submission = submissionService.getSubmissionById(id);
        return ResponseEntity.ok(submission); // 200 OK
    }

    // ------------------- GET MY LATEST SUBMISSION -------------------
    @GetMapping("/my/latest")
    public ResponseEntity<SubmissionDto> getMyLatestSubmission() {
        SubmissionDto submission = submissionService.getMyLatestSubmission();
        return ResponseEntity.ok(submission); // 200 OK
    }

    // ------------------- GET MY SUBMISSIONS -------------------
    @GetMapping("/my")
    public ResponseEntity<List<SubmissionDto>> getMySubmissions() {
        List<SubmissionDto> submissions = submissionService.getMySubmissions();
        return ResponseEntity.ok(submissions); // 200 OK
    }
}