package com.vn.beta_testing.feature.reward.DTO;

import java.util.Map;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardAnalyticsDTO {
    private Double approvalRate;
    private Double rejectionRate;
    private Double avgVerificationTime;
    private Double avgTransferTime;
    private Double totalRewardThisMonth;
    private Long activeTesters;
    private Map<String, Double> rewardByMonth;
}
