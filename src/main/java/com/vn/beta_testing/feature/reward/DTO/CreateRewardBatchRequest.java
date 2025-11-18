package com.vn.beta_testing.feature.reward.DTO;

import lombok.Data;

@Data
public class CreateRewardBatchRequest {
    private Long campaignId;
    private String note;
}
