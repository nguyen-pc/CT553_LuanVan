package com.vn.beta_testing.feature.test_execution.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.feature.test_execution.DTO.TesterSurveyDTO;
import com.vn.beta_testing.feature.test_execution.service.TesterSurveyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/campaign/tester-survey")
@RequiredArgsConstructor
public class TesterSurveyController {

    private final TesterSurveyService testerSurveyService;

    @PostMapping("/create")
    public ResponseEntity<TesterSurveyDTO> create(@RequestBody TesterSurveyDTO dto) {
        return ResponseEntity.ok(testerSurveyService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<TesterSurveyDTO>> getAll() {
        return ResponseEntity.ok(testerSurveyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TesterSurveyDTO> getById(@PathVariable Long id) {
        return testerSurveyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        testerSurveyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}