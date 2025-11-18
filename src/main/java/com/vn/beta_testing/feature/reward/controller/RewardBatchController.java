package com.vn.beta_testing.feature.reward.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.domain.RewardBatch;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.reward.DTO.CreateRewardBatchRequest;
import com.vn.beta_testing.feature.reward.DTO.RewardBatchResponse;
import com.vn.beta_testing.feature.reward.service.RewardBatchService;
import com.vn.beta_testing.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reward-batches")
@RequiredArgsConstructor
public class RewardBatchController {

    private final RewardBatchService rewardBatchService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RewardBatchResponse> createBatch(@RequestBody CreateRewardBatchRequest req) {

        User user = getCurrentUser();

        RewardBatch batch = rewardBatchService.createBatch(req, user.getCompanyProfile().getId());
        return ResponseEntity.ok(map(batch));
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<RewardBatchResponse>> getByCampaign(@PathVariable("campaignId") Long campaignId) {

        List<RewardBatch> batches = rewardBatchService.getByCampaignId(campaignId);

        List<RewardBatchResponse> result = batches.stream()
                .map(this::map)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<RewardBatchResponse> approveBatch(@PathVariable("id") Long id) {

        User user = getCurrentUser();

        RewardBatch batch = rewardBatchService.approveBatch(id, user.getId());
        return ResponseEntity.ok(map(batch));
    }


    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not logged in"));
        return userService.handleGetUserByUsername(email);
    }

    private RewardBatchResponse map(RewardBatch b) {
        RewardBatchResponse dto = new RewardBatchResponse();
        dto.setId(b.getId());
        dto.setCampaignId(b.getCampaignId());
        dto.setCompanyId(b.getCompanyId());
        dto.setStatus(b.getStatus());
        dto.setTotalAmount(b.getTotalAmount());
        dto.setNote(b.getNote());
        dto.setApprovedById(b.getApprovedById());
        dto.setApprovedAt(b.getApprovedAt());
        dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }
}
