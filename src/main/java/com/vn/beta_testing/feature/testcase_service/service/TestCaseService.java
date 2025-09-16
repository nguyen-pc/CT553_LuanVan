package com.vn.beta_testing.feature.testcase_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.TestCase;
import com.vn.beta_testing.feature.testcase_service.repository.TestCaseRepository;

@Service
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;

    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    public TestCase createTestCase(TestCase testCase) {
        return testCaseRepository.save(testCase);
    }

    public TestCase fetchTestCaseById(Long id) {
        return this.testCaseRepository.findById(id).orElse(null);
    }
    public TestCase updateTestCase(TestCase testCase) {
        TestCase existingTestCase = this.testCaseRepository.findById(testCase.getId()).orElse(null);
        if (existingTestCase == null) {
            throw new IllegalArgumentException("TestCase with id = " + testCase.getId() + " does not exist.");
        }
        existingTestCase.setTitle(testCase.getTitle());
        existingTestCase.setDescription(testCase.getDescription());
        existingTestCase.setPreCondition(testCase.getPreCondition());
        existingTestCase.setSteps(testCase.getSteps());
        existingTestCase.setExpectedResult(testCase.getExpectedResult());
        existingTestCase.setPriority(testCase.getPriority());
        // existingTestCase.setType(testCase.getType());
        // existingTestCase.setCreatedBy(testCase.getCreatedBy());
        // existingTestCase.setLastModifiedBy(testCase.getLastModifiedBy());
        return this.testCaseRepository.save(existingTestCase);
    }

    public void deleteTestCase(Long id) {
        this.testCaseRepository.deleteById(id);
    }
}
