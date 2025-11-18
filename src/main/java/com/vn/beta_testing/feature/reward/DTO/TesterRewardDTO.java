package com.vn.beta_testing.feature.reward.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vn.beta_testing.util.constant.TesterRewardStatus;
import lombok.Data;

@Data
public class TesterRewardDTO {
    private Long id;
    private Long testerId;
    private Long rewardBatchId;
    private BigDecimal amount;
    private TesterRewardStatus status;
    private String failureReason;
    private String evidenceUrl;
    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private LocalDateTime paidAt;
}