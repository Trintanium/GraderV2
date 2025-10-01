package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.TestCaseDto;
import com.example.graderbackend.service.TestCaseService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks/{taskId}/testcases")
public class TestCaseController {

    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    // ------------------- GET ALL TEST CASES FOR A TASK -------------------
    @GetMapping
    public ResponseEntity<List<TestCaseDto>> getTestCasesByProblem(@PathVariable Long taskId) {
        List<TestCaseDto> testCases = testCaseService.getTestCasesByProblemId(taskId);
        return ResponseEntity.ok(testCases); // 200 OK
    }

    // ------------------- GET TEST CASE BY ID -------------------
    @GetMapping("/{testcaseId}")
    public ResponseEntity<TestCaseDto> getTestCaseById(@PathVariable Long testcaseId) {
        TestCaseDto testCase = testCaseService.getTestCaseById(testcaseId);
        return ResponseEntity.ok(testCase); // 200 OK
    }

    // ------------------- CREATE TEST CASE -------------------
    @PostMapping
    public ResponseEntity<TestCaseDto> createTestCase(@PathVariable Long taskId, @RequestBody TestCaseDto dto) {
        dto.setProblemId(taskId);
        TestCaseDto created = testCaseService.createTestCase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    // ------------------- UPDATE TEST CASE -------------------
    @PutMapping("/{testcaseId}")
    public ResponseEntity<TestCaseDto> updateTestCase(@PathVariable Long testcaseId, @RequestBody TestCaseDto dto) {
        TestCaseDto updated = testCaseService.updateTestCase(testcaseId, dto);
        return ResponseEntity.ok(updated); // 200 OK
    }

    // ------------------- DELETE TEST CASE -------------------
    @DeleteMapping("/{testcaseId}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long testcaseId) {
        testCaseService.deleteTestCase(testcaseId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}