package com.vn.beta_testing.feature.reward.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AddTesterRewardRequest {

    private Long rewardBatchId;
    private Long testerId;

    private BigDecimal rewardAmount;
    private BigDecimal bonusAmount;

    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
}
