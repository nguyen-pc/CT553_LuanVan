package com.vn.beta_testing.feature.reward.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vn.beta_testing.util.constant.RewardBatchStatus;
import lombok.Data;

@Data
public class RewardBatchResponse {
    private Long id;
    private Long campaignId;
    private Long companyId;
    private RewardBatchStatus status;
    private BigDecimal totalAmount;
    private String note;
    private Long approvedById;
    private Long finalizedById; // admin đối soát cuối
    private LocalDateTime finalizedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}