package com.vn.beta_testing.feature.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.feature.auth_service.DTO.TesterStatisticSummaryDTO;
import com.vn.beta_testing.feature.auth_service.service.TesterStatisticService;
import com.vn.beta_testing.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/users")
public class TesterStatisticController {

    private final TesterStatisticService testerStatisticService;

    public TesterStatisticController(TesterStatisticService testerStatisticService) {
        this.testerStatisticService = testerStatisticService;
    }

    @GetMapping("/{userId}/statistic")
    @ApiMessage("Get tester summary statistics")
    public ResponseEntity<TesterStatisticSummaryDTO> getTesterStatistics(@PathVariable("userId") Long userId) {
        TesterStatisticSummaryDTO stats = testerStatisticService.getTesterStatistics(userId);
        return ResponseEntity.ok(stats);
    }
}