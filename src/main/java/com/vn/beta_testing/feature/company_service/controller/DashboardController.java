package com.vn.beta_testing.feature.company_service.controller;

import com.vn.beta_testing.feature.company_service.service.DashboardService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    @ApiMessage("Fetch dashboard overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview(
            @RequestParam(value = "type",  defaultValue = "global") String type,
            @RequestParam(value = "companyId", required = false) Long companyId) {
        Map<String, Object> result = dashboardService.getDashboardOverview(type, companyId);
        return ResponseEntity.ok(result);

    }
}