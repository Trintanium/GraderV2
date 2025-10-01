package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.ProblemDto;
import com.example.graderbackend.dto.entity.TagDto;
import com.example.graderbackend.entity.Difficulty;
import com.example.graderbackend.service.ProblemService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    // ------------------- GET ALL PROBLEMS -------------------
    @GetMapping
    public ResponseEntity<List<ProblemDto>> getAllProblems() {
        List<ProblemDto> problems = problemService.getAllProblem();
        return ResponseEntity.ok(problems); // 200 OK
    }

    // ------------------- GET PROBLEM BY ID -------------------
    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        ProblemDto problem = problemService.getProblemById(id);
        return ResponseEntity.ok(problem); // 200 OK
    }

    // ------------------- GET PROBLEM TAGS -------------------
    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TagDto>> getProblemTags(@PathVariable Long id) {
        List<TagDto> tags = problemService.getTagsByProblemId(id);
        return ResponseEntity.ok(tags); // 200 OK
    }

    // ------------------- CREATE PROBLEM -------------------
    @PostMapping
    public ResponseEntity<ProblemDto> createProblem(
            @RequestParam String title,
            @RequestParam Difficulty difficulty,
            @RequestPart("pdf") MultipartFile pdf
    ) throws IOException {
        ProblemDto created = problemService.createProblemWithPdf(title, difficulty, pdf);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 201 Created
    }

    // ------------------- UPDATE PROBLEM -------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProblemDto> updateProblem(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam Difficulty difficulty,
            @RequestPart(value = "pdf", required = false) MultipartFile pdf,
            @RequestParam(value = "tags", required = false) List<Long> tags
    ) throws IOException {
        ProblemDto updated = problemService.updateProblem(id, title, difficulty.name(), pdf, tags);
        return ResponseEntity.ok(updated); // 200 OK
    }

    // ------------------- DELETE PROBLEM -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}