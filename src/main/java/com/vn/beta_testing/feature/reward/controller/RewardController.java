package com.vn.beta_testing.feature.reward.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vn.beta_testing.feature.reward.DTO.*;
import com.vn.beta_testing.feature.reward.service.RewardService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.domain.TesterReward;

@RestController
@RequestMapping("/api/v1/reward")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @PostMapping("/request")
    @ApiMessage("Tester gửi yêu cầu nhận thưởng")
    public ResponseEntity<TesterReward> requestReward(@RequestParam("userId") Long userId,
            @RequestBody RewardRequestDTO dto) {
        return ResponseEntity.ok(rewardService.requestReward(userId, dto));
    }

    @PutMapping("/verify/{rewardId}")
    @ApiMessage("Admin xác minh yêu cầu thưởng")
    public ResponseEntity<TesterReward> verifyReward(@PathVariable("rewardId") Long rewardId,
            @RequestParam("adminName") String adminName) {
        return ResponseEntity.ok(rewardService.verifyReward(rewardId, adminName));
    }

    @PutMapping("/approve/{rewardId}")
    @ApiMessage("Company duyệt & chuyển thưởng")
    public ResponseEntity<TesterReward> approveReward(@PathVariable("rewardId") Long rewardId,
            @RequestParam("companyName") String companyName,
            @RequestParam(value = "proofUrl", required = false) String proofUrl) {
        return ResponseEntity.ok(rewardService.approveReward(rewardId, companyName, proofUrl));
    }

    @PutMapping("/confirm/{rewardId}")
    @ApiMessage("Admin xác nhận chuyển thưởng thành công")
    public ResponseEntity<TesterReward> confirmReward(@PathVariable("rewardId") Long rewardId,
            @RequestParam("adminName") String adminName) {
        return ResponseEntity.ok(rewardService.confirmReward(rewardId, adminName));
    }

    @GetMapping("/analytics")
    @ApiMessage("Lấy thống kê dashboard thưởng")
    public ResponseEntity<RewardAnalyticsDTO> getRewardAnalytics() {
        return ResponseEntity.ok(rewardService.getRewardAnalytics());
    }
}
