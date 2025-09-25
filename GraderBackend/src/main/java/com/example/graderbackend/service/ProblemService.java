/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.ProblemDto
 *  com.example.graderbackend.dto.entity.TagDto
 *  com.example.graderbackend.entity.Difficulty
 *  com.example.graderbackend.service.ProblemService
 *  org.springframework.web.multipart.MultipartFile
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.entity.ProblemDto;
import com.example.graderbackend.dto.entity.TagDto;
import com.example.graderbackend.entity.Difficulty;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProblemService {
    public List<ProblemDto> getAllProblem();

    public ProblemDto getProblemById(Long var1);

    public List<TagDto> getTagsByProblemId(Long var1);

    public ProblemDto createProblemWithPdf(String var1, Difficulty var2, MultipartFile var3) throws IOException;

    public ProblemDto updateProblem(Long var1, String var2, String var3, MultipartFile var4, List<Long> var5) throws IOException;

    public void deleteProblem(Long var1);
}

