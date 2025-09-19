/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.controller.ProblemController
 *  com.example.graderbackend.dto.entity.ProblemDto
 *  com.example.graderbackend.dto.entity.TagDto
 *  com.example.graderbackend.entity.Difficulty
 *  com.example.graderbackend.service.ProblemService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RequestPart
 *  org.springframework.web.bind.annotation.RestController
 *  org.springframework.web.multipart.MultipartFile
 */
package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.ProblemDto;
import com.example.graderbackend.dto.entity.TagDto;
import com.example.graderbackend.entity.Difficulty;
import com.example.graderbackend.service.ProblemService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value={"/problem"})
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @GetMapping
    public ResponseEntity<List<ProblemDto>> getAllProblems() {
        return ResponseEntity.ok(this.problemService.getAllProblem());
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable Long id) {
        System.out.println(this.problemService.getProblemById(id));
        return ResponseEntity.ok(this.problemService.getProblemById(id));
    }

    @GetMapping(value={"/{id}/tags"})
    public ResponseEntity<List<TagDto>> getProblemTags(@PathVariable Long id) {
        return ResponseEntity.ok(this.problemService.getTagsByProblemId(id));
    }

    @PostMapping
    public ResponseEntity<ProblemDto> createProblem(@RequestParam String title, @RequestParam Difficulty difficulty, @RequestPart(value="pdf") MultipartFile pdf) throws IOException {
        return ResponseEntity.ok(this.problemService.createProblemWithPdf(title, difficulty, pdf));
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ProblemDto> updateProblem(@PathVariable Long id, @RequestPart(value="title") String title, @RequestPart(value="difficulty") String difficulty, @RequestPart(value="pdf", required=false) MultipartFile pdf, @RequestPart(value="tags", required=false) List<Long> tags) throws IOException {
        return ResponseEntity.ok(this.problemService.updateProblem(id, title, difficulty, pdf, tags));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        this.problemService.deleteProblem(id);
        return ResponseEntity.ok().build();
    }
}

