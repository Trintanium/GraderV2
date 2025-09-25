/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.controller.ProblemTagController
 *  com.example.graderbackend.dto.entity.ProblemTagDto
 *  com.example.graderbackend.service.ProblemTagService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.example.graderbackend.controller;

import com.example.graderbackend.dto.entity.ProblemTagDto;
import com.example.graderbackend.service.ProblemTagService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/problem-tag"})
public class ProblemTagController {
    private final ProblemTagService problemTagService;

    @Autowired
    public ProblemTagController(ProblemTagService problemTagService) {
        this.problemTagService = problemTagService;
    }

    @GetMapping
    public List<ProblemTagDto> getProblemTags(@RequestParam(required=false) Long problemId) {
        return this.problemTagService.getTagsByProblem(problemId);
    }

    @PostMapping
    public ProblemTagDto createProblemTag(@RequestBody ProblemTagDto dto) {
        return this.problemTagService.createProblemTag(dto.getProblemId(), dto.getTagId());
    }

    @PutMapping(value={"/update/{problemId}"})
    public void updateProblemTags(@PathVariable Long problemId, @RequestBody List<Long> tagIds) {
        this.problemTagService.updateProblemTags(problemId, tagIds);
    }

    @DeleteMapping(value={"/{id}"})
    public void deleteProblemTag(@PathVariable Long id) {
        this.problemTagService.deleteProblemTag(id);
    }
}

