/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.dto.entity.TestCaseDto
 *  com.example.graderbackend.service.TestCaseService
 */
package com.example.graderbackend.service;

import com.example.graderbackend.dto.entity.TestCaseDto;
import java.util.List;

public interface TestCaseService {
    public List<TestCaseDto> getAllTestCases();

    public TestCaseDto getTestCaseById(Long var1);

    public List<TestCaseDto> getTestCasesByProblemId(Long var1);

    public TestCaseDto createTestCase(TestCaseDto var1);

    public TestCaseDto updateTestCase(Long var1, TestCaseDto var2);

    public void deleteTestCase(Long var1);
}

