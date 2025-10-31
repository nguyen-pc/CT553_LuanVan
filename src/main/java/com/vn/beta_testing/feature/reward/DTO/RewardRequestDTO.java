package com.vn.beta_testing.feature.reward.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardRequestDTO {
    private Long testerCampaignId;
    private Double amount;
}
