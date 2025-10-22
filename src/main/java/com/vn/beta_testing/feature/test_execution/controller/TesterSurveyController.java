package com.vn.beta_testing.feature.test_execution.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.feature.test_execution.DTO.TesterResponseDTO;
import com.vn.beta_testing.feature.test_execution.DTO.TesterSurveyDTO;
import com.vn.beta_testing.feature.test_execution.service.TesterSurveyService;
import com.vn.beta_testing.util.annotation.ApiMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/campaign/tester-survey")
@RequiredArgsConstructor
public class TesterSurveyController {

    private final TesterSurveyService testerSurveyService;

    @PostMapping("/create")
    @ApiMessage("Tester survey created successfully")
    public ResponseEntity<TesterSurveyDTO> create(@RequestBody TesterSurveyDTO dto) {
        return ResponseEntity.ok(testerSurveyService.create(dto));
    }

    @GetMapping
    @ApiMessage("Get all tester surveys successfully")
    public ResponseEntity<List<TesterSurveyDTO>> getAll() {
        return ResponseEntity.ok(testerSurveyService.getAll());
    }

    @GetMapping("/status")
    @ApiMessage("Get tester survey status successfully")
    public ResponseEntity<TesterSurveyDTO> getByStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("surveyId") Long surveyId) {
        TesterSurveyDTO dto = testerSurveyService.getByStatus(userId, surveyId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/survey/{surveyId}")
    @ApiMessage("Get tester surveys by survey ID successfully")
    public ResponseEntity<List<TesterResponseDTO>> getBySurveyId(@PathVariable("surveyId") Long surveyId) {
        return ResponseEntity.ok(testerSurveyService.getBySurveyId(surveyId));
    }
    @GetMapping("/{id}")
    @ApiMessage("Get tester survey by ID successfully")
    public ResponseEntity<TesterSurveyDTO> getById(@PathVariable("id") Long id) {
        return testerSurveyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Tester survey deleted successfully")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        testerSurveyService.delete(id);
        return ResponseEntity.noContent().build();
    }


}