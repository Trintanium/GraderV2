package com.example.graderbackend.service.impl;

import com.example.graderbackend.dto.entity.TestCaseDto;
import com.example.graderbackend.entity.Problem;
import com.example.graderbackend.entity.TestCase;
import com.example.graderbackend.entity.Type;
import com.example.graderbackend.exception.TestCaseNotFoundException;
import com.example.graderbackend.repository.TestCaseRepository;
import com.example.graderbackend.service.ModelMapperService;
import com.example.graderbackend.service.TestCaseService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestCaseServiceImpl implements TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final ModelMapperService modelMapperService;

    public TestCaseServiceImpl(TestCaseRepository testCaseRepository, ModelMapperService modelMapperService) {
        this.testCaseRepository = testCaseRepository;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public List<TestCaseDto> getAllTestCases() {
        return modelMapperService.toListDto(testCaseRepository.findAll(), TestCaseDto.class);
    }

    @Override
    public TestCaseDto getTestCaseById(Long id) {
        TestCase testCase = testCaseRepository.findById(id)
                .orElseThrow(() -> new TestCaseNotFoundException(id));
        return modelMapperService.toDto(testCase, TestCaseDto.class);
    }

    @Override
    public List<TestCaseDto> getTestCasesByProblemId(Long problemId) {
        List<TestCase> testCases = testCaseRepository.findByProblem_Id(problemId);
        return modelMapperService.toListDto(testCases, TestCaseDto.class);
    }

    @Override
    public TestCaseDto createTestCase(TestCaseDto dto) {
        TestCase testCase = new TestCase();
        testCase.setProblem(new Problem());
        testCase.getProblem().setId(dto.getProblemId());
        testCase.setInput(dto.getInput());
        testCase.setOutput(dto.getOutput());
        testCase.setType(Type.valueOf(dto.getType()));
        testCase.setCreatedAt(LocalDateTime.now());

        TestCase saved = testCaseRepository.save(testCase);
        return modelMapperService.toDto(saved, TestCaseDto.class);
    }

    @Override
    public TestCaseDto updateTestCase(Long id, TestCaseDto dto) {
        TestCase updated = testCaseRepository.findById(id)
                .map(testCase -> {
                    testCase.setInput(dto.getInput());
                    testCase.setOutput(dto.getOutput());
                    testCase.setType(Type.valueOf(dto.getType()));
                    testCase.setUpdatedAt(LocalDateTime.now());
                    return testCaseRepository.save(testCase);
                })
                .orElseThrow(() -> new TestCaseNotFoundException(id));

        return modelMapperService.toDto(updated, TestCaseDto.class);
    }

    @Override
    public void deleteTestCase(Long id) {
        if (!testCaseRepository.existsById(id)) {
            throw new TestCaseNotFoundException(id);
        }
        testCaseRepository.deleteById(id);
    }
}