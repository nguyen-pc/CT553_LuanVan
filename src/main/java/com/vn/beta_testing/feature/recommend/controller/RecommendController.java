package com.vn.beta_testing.feature.recommend.controller;

import com.vn.beta_testing.domain.response.RestResponse;
import com.vn.beta_testing.feature.recommend.dto.RecommendResultDTO;
import com.vn.beta_testing.feature.recommend.service.PythonRecommendClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recommend")
public class RecommendController {

    private final PythonRecommendClient pythonRecommendClient;

    public RecommendController(PythonRecommendClient pythonRecommendClient) {
        this.pythonRecommendClient = pythonRecommendClient;
    }

    @GetMapping("/campaigns/{userId}")
    public ResponseEntity<List<RecommendResultDTO>> recommendCampaigns(@PathVariable("userId") Long userId) {
        List<RecommendResultDTO> result = pythonRecommendClient.getRecommendations(userId);
        return ResponseEntity.ok(result);
    }
}
