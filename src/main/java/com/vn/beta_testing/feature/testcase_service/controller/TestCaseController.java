package com.vn.beta_testing.feature.testcase_service.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import com.turkraft.springfilter.boot.Filter;
import com.vn.beta_testing.domain.TestCase;
import com.vn.beta_testing.domain.TestScenario;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.testcase_service.service.TestCaseService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class TestCaseController {
    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    @ApiMessage("Get testcase by scenario id")
    @GetMapping("/usecase/test_scenario/{scenarioId}/testcase")
    public ResponseEntity<ResultPaginationDTO> getTestCaseByUseCase(@PathVariable("scenarioId") Long scenarioId,
            @Filter Specification<TestCase> spec,
            Pageable pageable) {
        Specification<TestCase> scenarioSpec = (root, query, builder) -> builder.equal(
                root.get("testScenario").get("id"),
                scenarioId);
        Specification<TestCase> finalSpec = Specification.where(scenarioSpec).and(spec);
        return ResponseEntity.ok().body(this.testCaseService.fetchAll(finalSpec, pageable));
    }

    @PostMapping("usecase/test_scenario/testcase/create")
    @ApiMessage("Create a new test case")
    public ResponseEntity<TestCase> createTestCase(@RequestBody TestCase testCase) {

        TestCase createdTestCase = testCaseService.createTestCase(testCase);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTestCase);
    }

    @PutMapping("usecase/test_scenario/testcase/update/{id}")
    @ApiMessage("Update test case")
    public ResponseEntity<TestCase> updateTestCase(@PathVariable("id") Long id, @RequestBody TestCase testCase) {
        TestCase existingTestCase = this.testCaseService.fetchTestCaseById(id);
        if (existingTestCase == null) {
            throw new IdInvalidException("Test case with id = " + id + " does not exist.");
        }
        TestCase updatedTestCase = this.testCaseService.updateTestCase(testCase);
        return ResponseEntity.ok(updatedTestCase);
    }

    @GetMapping("usecase/test_scenario/testcase/{id}")
    @ApiMessage("Get test case by id")
    public ResponseEntity<TestCase> getTestCaseById(@PathVariable("id") Long id) {
        TestCase testCase = testCaseService.fetchTestCaseById(id);
        if (testCase == null) {
            throw new IdInvalidException("Test case with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(testCase);
    }

    @DeleteMapping("usecase/test_scenario/testcase/delete/{id}")
    @ApiMessage("Delete test case")
    public ResponseEntity<Void> deleteTestCase(@PathVariable("id") Long id) {
        TestCase existingTestCase = this.testCaseService.fetchTestCaseById(id);
        if (existingTestCase == null) {
            throw new IdInvalidException("Test case with id = " + id + " does not exist.");
        }
        this.testCaseService.deleteTestCase(id);
        return ResponseEntity.noContent().build();
    }
}
