package com.vn.beta_testing.feature.reward.controller;

import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.domain.RewardBatchEvidence;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.reward.DTO.UploadEvidenceRequest;
import com.vn.beta_testing.feature.reward.service.RewardBatchService;
import com.vn.beta_testing.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reward-evidence")
@RequiredArgsConstructor
public class RewardEvidenceController {

    private final RewardBatchService rewardBatchService;
    private final UserService userService;

    @PostMapping("/{batchId}")
    public RewardBatchEvidence uploadEvidence(
            @PathVariable("batchId") Long batchId,
            @RequestBody UploadEvidenceRequest req) {

        User user = getCurrentUser();
        return rewardBatchService.uploadEvidence(batchId, req, user.getId());
    }

    @GetMapping("/{batchId}")
    public RewardBatchEvidence getEvidenceByBatchId(@PathVariable("batchId") Long batchId) {
        return rewardBatchService.getEvidenceByBatchId(batchId);
    }   

    /** PRIVATE: lấy user login */
    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not logged in"));
        return userService.handleGetUserByUsername(email);
    }
}
