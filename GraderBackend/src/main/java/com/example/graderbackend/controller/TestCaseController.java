/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.controller.TestCaseController
 *  com.example.graderbackend.dto.entity.TestCaseDto
 *  com.example.graderbackend.service.TestCaseService
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.TestCaseDto;
import com.example.graderbackend.service.TestCaseService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/tasks/{taskId}/testcases"})
public class TestCaseController {
    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @GetMapping
    public ResponseEntity<List<TestCaseDto>> getTestCasesByProblem(@PathVariable Long taskId) {
        return ResponseEntity.ok(this.testCaseService.getTestCasesByProblemId(taskId));
    }

    @GetMapping(value={"/{testcaseId}"})
    public ResponseEntity<TestCaseDto> getTestCaseById(@PathVariable Long testcaseId) {
        return ResponseEntity.ok(this.testCaseService.getTestCaseById(testcaseId));
    }

    @PostMapping
    public ResponseEntity<TestCaseDto> createTestCase(@PathVariable Long taskId, @RequestBody TestCaseDto dto) {
        dto.setProblemId(taskId);
        return ResponseEntity.ok(this.testCaseService.createTestCase(dto));
    }

    @PutMapping(value={"/{testcaseId}"})
    public ResponseEntity<TestCaseDto> updateTestCase(@PathVariable Long testcaseId, @RequestBody TestCaseDto dto) {
        return ResponseEntity.ok(this.testCaseService.updateTestCase(testcaseId, dto));
    }

    @DeleteMapping(value={"/{testcaseId}"})
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long testcaseId) {
        this.testCaseService.deleteTestCase(testcaseId);
        return ResponseEntity.noContent().build();
    }
}

