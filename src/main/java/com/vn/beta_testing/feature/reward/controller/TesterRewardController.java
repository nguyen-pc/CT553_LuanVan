package com.vn.beta_testing.feature.reward.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.domain.TesterReward;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.reward.DTO.AddTesterRewardRequest;
import com.vn.beta_testing.feature.reward.DTO.UpdateTesterRewardStatusRequest;
import com.vn.beta_testing.feature.reward.service.RewardBatchService;
import com.vn.beta_testing.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tester-rewards")
@RequiredArgsConstructor
public class TesterRewardController {

    private final RewardBatchService rewardBatchService;
    private final UserService userService;

    @GetMapping("/me")
    public List<TesterReward> myRewards() {
        User user = getCurrentUser();
        return rewardBatchService.getTesterHistory(user.getId());
    }

    @PostMapping
    public TesterReward addTesterReward(@RequestBody AddTesterRewardRequest req) {
        return rewardBatchService.addTesterReward(req);
    }

    @GetMapping("/batch/{batchId}")
    public List<TesterReward> getRewardsByBatchId(@PathVariable("batchId") Long batchId) {
        return rewardBatchService.getRewardsByBatchId(batchId);
    }

    @PostMapping("/{rewardId}/status")
    public TesterReward updateStatus(
            @PathVariable("rewardId") Long rewardId,
            @RequestBody UpdateTesterRewardStatusRequest req) {

        return rewardBatchService.updateTesterReward(rewardId, req);
    }

    /** COMMON: lấy user login */
    private User getCurrentUser() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User not logged in"));
        return userService.handleGetUserByUsername(email);
    }
}
