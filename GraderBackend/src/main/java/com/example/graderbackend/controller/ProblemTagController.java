package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.ProblemTagDto;
import com.example.graderbackend.service.ProblemTagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problem-tag")
public class ProblemTagController {

    private final ProblemTagService problemTagService;

    @Autowired
    public ProblemTagController(ProblemTagService problemTagService) {
        this.problemTagService = problemTagService;
    }

    // ------------------- GET PROBLEM TAGS -------------------
    @GetMapping
    public ResponseEntity<List<ProblemTagDto>> getProblemTags(@RequestParam(required = false) Long problemId) {
        List<ProblemTagDto> tags = problemTagService.getTagsByProblem(problemId);
        return ResponseEntity.ok(tags); // 200 OK
    }

    // ------------------- CREATE PROBLEM TAG -------------------
    @PostMapping
    public ResponseEntity<ProblemTagDto> createProblemTag(@RequestBody ProblemTagDto dto) {
        ProblemTagDto created = problemTagService.createProblemTag(dto.getProblemId(), dto.getTagId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    // ------------------- UPDATE PROBLEM TAGS -------------------
    @PutMapping("/update/{problemId}")
    public ResponseEntity<Void> updateProblemTags(@PathVariable Long problemId, @RequestBody List<Long> tagIds) {
        problemTagService.updateProblemTags(problemId, tagIds);
        return ResponseEntity.ok().build(); // 200 OK
    }

    // ------------------- DELETE PROBLEM TAG -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblemTag(@PathVariable Long id) {
        problemTagService.deleteProblemTag(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}