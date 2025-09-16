package com.vn.beta_testing.feature.testcase_service.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.TestScenario;
import com.vn.beta_testing.feature.testcase_service.service.TestScenarioService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class TestScenarioController {
    private final TestScenarioService testScenarioService;

    public TestScenarioController(TestScenarioService testScenarioService) {
        this.testScenarioService = testScenarioService;
    }

    

    @PostMapping("/testscenario/create")
    @ApiMessage("Create a new test scenario")
    public ResponseEntity<TestScenario> createTestScenario(@RequestBody TestScenario testScenario) {
        TestScenario createdTestScenario = testScenarioService.createTestScenario(testScenario);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestScenario);
    }

    @PutMapping("/testscenario/update/{id}")
    @ApiMessage("Update test scenario")
    public ResponseEntity<TestScenario> updateTestScenario(@PathVariable("id") Long id, @RequestBody TestScenario testScenario) {
        TestScenario existingTestScenario = this.testScenarioService.fetchTestScenarioById(id);
        if (existingTestScenario == null) {
            throw new IdInvalidException("Test scenario with id = " + id + " does not exist.");
        }
        TestScenario updatedTestScenario = this.testScenarioService.updateTestScenario(testScenario);
        return ResponseEntity.ok(updatedTestScenario);
    }

    @GetMapping("/testscenario/{id}")
    @ApiMessage("Get test scenario by id")
    public ResponseEntity<TestScenario> getTestScenarioById(@PathVariable("id") Long id) {
        TestScenario testScenario = testScenarioService.fetchTestScenarioById(id);
        if (testScenario == null) {
            throw new IdInvalidException("Test scenario with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(testScenario);
    }

    @DeleteMapping("/testscenario/delete/{id}")
    @ApiMessage("Delete test scenario") 
    public ResponseEntity<Void> deleteTestScenario(@PathVariable("id") Long id) {
        TestScenario existingTestScenario = this.testScenarioService.fetchTestScenarioById(id);
        if (existingTestScenario == null) {
            throw new IdInvalidException("Test scenario with id = " + id + " does not exist.");
        }
        // Assuming a delete method exists in the service
        this.testScenarioService.deleteTestScenario(id);
        return ResponseEntity.noContent().build();
    }
}
