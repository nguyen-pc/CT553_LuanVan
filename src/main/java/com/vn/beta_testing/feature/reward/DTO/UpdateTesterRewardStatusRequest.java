package com.vn.beta_testing.feature.reward.DTO;

import com.vn.beta_testing.util.constant.TesterRewardStatus;

import lombok.Data;

@Data
public class UpdateTesterRewardStatusRequest {
    private TesterRewardStatus status;
    private String failureReason;
    private String evidenceUrl;
}