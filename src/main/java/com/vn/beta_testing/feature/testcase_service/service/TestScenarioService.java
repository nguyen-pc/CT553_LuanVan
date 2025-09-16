package com.vn.beta_testing.feature.testcase_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.TestScenario;
import com.vn.beta_testing.feature.testcase_service.repository.TestScenarioRepository;

@Service
public class TestScenarioService {
    private final TestScenarioRepository testScenarioRepository;

    public TestScenarioService(TestScenarioRepository testScenarioRepository) {
        this.testScenarioRepository = testScenarioRepository;
    }

    public TestScenario createTestScenario(TestScenario testScenario) {
        return testScenarioRepository.save(testScenario);
    }

    public TestScenario fetchTestScenarioById(Long id) {
        return this.testScenarioRepository.findById(id).orElse(null);
    }

    public TestScenario updateTestScenario(TestScenario testScenario) {
        TestScenario existingTestScenario = this.testScenarioRepository.findById(testScenario.getId()).orElse(null);
        if (existingTestScenario == null) {
            throw new IllegalArgumentException("TestScenario with id = " + testScenario.getId() + " does not exist.");
        }
        existingTestScenario.setTitle(testScenario.getTitle());
        existingTestScenario.setDescription(testScenario.getDescription());
        return this.testScenarioRepository.save(existingTestScenario);
    }
    public void deleteTestScenario(Long id) {
        this.testScenarioRepository.deleteById(id);
    }
}
