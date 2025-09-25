/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.ProblemTagDto
 *  com.example.graderbackend.service.ProblemTagService
 *  org.springframework.transaction.annotation.Transactional
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.entity.ProblemTagDto;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ProblemTagService {
    public List<ProblemTagDto> getTagsByProblem(Long var1);

    @Transactional
    public ProblemTagDto createProblemTag(Long var1, Long var2);

    @Transactional
    public void updateProblemTags(Long var1, List<Long> var2);

    public void deleteProblemTag(Long var1);
}

