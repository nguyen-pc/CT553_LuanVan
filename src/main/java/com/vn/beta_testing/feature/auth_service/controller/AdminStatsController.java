package com.vn.beta_testing.feature.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.feature.auth_service.DTO.AdminDashboardDTO;
import com.vn.beta_testing.feature.auth_service.service.AdminStatsService;

@RestController
@RequestMapping("/api/v1/admin/stats")
public class AdminStatsController {

    private final AdminStatsService statsService;

    public AdminStatsController(AdminStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/overview")
    public ResponseEntity<AdminDashboardDTO> getOverview() {
        return ResponseEntity.ok(statsService.getOverviewStats());
    }
}