package com.vn.beta_testing.feature.test_execution.controller;


import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.domain.TestExecution;
import com.vn.beta_testing.feature.test_execution.DTO.TestExecutionDTO;
import com.vn.beta_testing.feature.test_execution.service.TestExecutionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test-execution")
@RequiredArgsConstructor
public class TestExecutionController {

    private final TestExecutionService testExecutionService;

    @GetMapping
    public ResponseEntity<List<TestExecutionDTO>> getAll() {
        return ResponseEntity.ok(testExecutionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestExecutionDTO> getById(@PathVariable("id") Long id) {
        return testExecutionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<TestExecutionDTO>> getByCampaign(@PathVariable("campaignId") Long campaignId) {
        return ResponseEntity.ok(testExecutionService.getByCampaign(campaignId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TestExecutionDTO>> getByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(testExecutionService.getByUser(userId));
    }

    @GetMapping("/testcase/{testCaseId}")
    public ResponseEntity<List<TestExecutionDTO>> getByTestCase(@PathVariable("testCaseId") Long testCaseId) {
        return ResponseEntity.ok(testExecutionService.getByTestCase(testCaseId));
    }

    @GetMapping("/campaign/{campaignId}/user/{userId}")
    public ResponseEntity<List<TestExecutionDTO>> getByCampaignAndUser(
            @PathVariable("campaignId") Long campaignId, @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(testExecutionService.getByCampaignAndUser(campaignId, userId));
    }

    @GetMapping("/campaign/{campaignId}/testcase/{testCaseId}")
    public ResponseEntity<List<TestExecutionDTO>> getByCampaignAndTestCase(
            @PathVariable("campaignId") Long campaignId, @PathVariable("testCaseId") Long testCaseId) {
        return ResponseEntity.ok(testExecutionService.getByCampaignAndTestCase(campaignId, testCaseId));
    }

    @PostMapping
    public ResponseEntity<TestExecutionDTO> create(@RequestBody TestExecution testExecution) {
        return ResponseEntity.ok(testExecutionService.createOrUpdate(testExecution));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestExecutionDTO> update(@PathVariable("id") Long id, @RequestBody TestExecution testExecution) {
        return ResponseEntity.ok(testExecutionService.update(id, testExecution));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        testExecutionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}