package com.vn.beta_testing.feature.reward.scheduler;

import java.time.*;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.vn.beta_testing.domain.TesterReward;
import com.vn.beta_testing.feature.reward.repository.TesterRewardRepository;
import com.vn.beta_testing.util.constant.RewardStatus;

@Component
public class RewardSlaScheduler {

    private final TesterRewardRepository rewardRepo;

    public RewardSlaScheduler(TesterRewardRepository rewardRepo) {
        this.rewardRepo = rewardRepo;
    }

    @Scheduled(cron = "0 0 * * * *") // chạy mỗi giờ
    public void checkSlaViolations() {
        Instant now = Instant.now();
        List<TesterReward> rewards = rewardRepo.findAll();

        for (TesterReward r : rewards) {
            if (r.getStatus() == RewardStatus.REQUESTED &&
                    Duration.between(r.getRequestedAt(), now).toHours() > 48) {
                r.setAdminSlaViolated(true);
            }

            if (r.getStatus() == RewardStatus.APPROVED &&
                    Duration.between(r.getApprovedAt(), now).toHours() > 72) {
                r.setCompanySlaViolated(true);
            }
        }
        rewardRepo.saveAll(rewards);
    }
}
