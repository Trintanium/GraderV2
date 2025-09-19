/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.TestCaseDto
 *  com.example.graderbackend.entity.Problem
 *  com.example.graderbackend.entity.TestCase
 *  com.example.graderbackend.entity.Type
 *  com.example.graderbackend.repository.TestCaseRepository
 *  com.example.graderbackend.service.ModelMapperService
 *  com.example.graderbackend.service.TestCaseService
 *  com.example.graderbackend.service.impl.TestCaseServiceImpl
 *  org.springframework.stereotype.Service
 */
package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.TestCaseDto;
import com.example.graderbackend.entity.Problem;
import com.example.graderbackend.entity.TestCase;
import com.example.graderbackend.entity.Type;
import com.example.graderbackend.repository.TestCaseRepository;
import com.example.graderbackend.service.ModelMapperService;
import com.example.graderbackend.service.TestCaseService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestCaseServiceImpl
implements TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final ModelMapperService modelMapperService;

    public TestCaseServiceImpl(TestCaseRepository testCaseRepository, ModelMapperService modelMapperService) {
        this.testCaseRepository = testCaseRepository;
        this.modelMapperService = modelMapperService;
    }

    public List<TestCaseDto> getAllTestCases() {
        return this.modelMapperService.toListDto(this.testCaseRepository.findAll(), TestCaseDto.class);
    }

    public TestCaseDto getTestCaseById(Long id) {
        TestCase testCase = (TestCase)this.testCaseRepository.findById(id).orElseThrow(() -> new RuntimeException("TestCase not found with id " + id));
        return (TestCaseDto)this.modelMapperService.toDto(testCase, TestCaseDto.class);
    }

    public List<TestCaseDto> getTestCasesByProblemId(Long problemId) {
        List testCases = this.testCaseRepository.findByProblem_Id(problemId);
        return this.modelMapperService.toListDto(testCases, TestCaseDto.class);
    }

    public TestCaseDto createTestCase(TestCaseDto dto) {
        TestCase testCase = new TestCase();
        testCase.setProblem(new Problem());
        testCase.getProblem().setId(dto.getProblemId());
        testCase.setInput(dto.getInput());
        testCase.setOutput(dto.getOutput());
        testCase.setType(Type.valueOf((String)dto.getType()));
        testCase.setCreatedAt(LocalDateTime.now());
        TestCase saved = (TestCase)this.testCaseRepository.save(testCase);
        return (TestCaseDto)this.modelMapperService.toDto(saved, TestCaseDto.class);
    }

    public TestCaseDto updateTestCase(Long id, TestCaseDto dto) {
        TestCase updated = this.testCaseRepository.findById(id).map(testCase -> {
            testCase.setInput(dto.getInput());
            testCase.setOutput(dto.getOutput());
            testCase.setType(Type.valueOf((String)dto.getType()));
            testCase.setUpdatedAt(LocalDateTime.now());
            return (TestCase)this.testCaseRepository.save(testCase);
        }).orElseThrow(() -> new RuntimeException("TestCase not found with id " + id));
        return (TestCaseDto)this.modelMapperService.toDto(updated, TestCaseDto.class);
    }

    public void deleteTestCase(Long id) {
        if (!this.testCaseRepository.existsById(id)) {
            throw new RuntimeException("TestCase not found with id " + id);
        }
        this.testCaseRepository.deleteById(id);
    }
}

