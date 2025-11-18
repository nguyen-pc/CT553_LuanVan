package com.vn.beta_testing.feature.reward.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.domain.RewardBatch;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.reward.DTO.RewardBatchResponse;
import com.vn.beta_testing.feature.reward.service.RewardBatchService;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/reward-batches")
@RequiredArgsConstructor
public class AdminRewardBatchController {

    private final RewardBatchService rewardBatchService;
    private final UserService userService;

    /**
     * =============================
     * 1. ADMIN GET LIST ALL BATCHES
     * =============================
     */
    @GetMapping("")
    public ResponseEntity<List<RewardBatchResponse>> getAllBatches() {
        List<RewardBatch> list = rewardBatchService.getAllBatchesForAdmin();

        List<RewardBatchResponse> result = list.stream()
                .map(this::map)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * =============================
     * 2. ADMIN FINALIZE A BATCH
     * =============================
     */
    @PostMapping("/{id}/finalize")
    public ResponseEntity<RewardBatchResponse> finalizeRewardBatch(@PathVariable("id") Long id) {

        User admin = getCurrentUser(); // lấy admin hiện tại

        RewardBatch batch = rewardBatchService.finalizeBatch(id, admin.getId());

        return ResponseEntity.ok(map(batch));
    }

    // ======================================
    // COMMON UTILS
    // ======================================
    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Not logged in"));
        return userService.handleGetUserByUsername(email);
    }

    private RewardBatchResponse map(RewardBatch b) {
        RewardBatchResponse res = new RewardBatchResponse();
        res.setId(b.getId());
        res.setCampaignId(b.getCampaignId());
        res.setCompanyId(b.getCompanyId());
        res.setStatus(b.getStatus());
        res.setTotalAmount(b.getTotalAmount());
        res.setNote(b.getNote());
        res.setApprovedById(b.getApprovedById());
        res.setApprovedAt(b.getApprovedAt());
        res.setCreatedAt(b.getCreatedAt());
        res.setFinalizedById(b.getFinalizedById());
        res.setFinalizedAt(b.getFinalizedAt());
        return res;
    }
}
