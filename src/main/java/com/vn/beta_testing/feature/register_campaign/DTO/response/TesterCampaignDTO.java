package com.vn.beta_testing.feature.register_campaign.DTO.response;


import java.time.Instant;

import com.vn.beta_testing.util.constant.TesterCampaignStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TesterCampaignDTO {

    private Long id;
    private TesterCampaignStatus status;
    private Integer progress;
    private String roleInCampaign;
    private Instant joinDate;
    private Instant completionDate;
    private String note;

    // Chỉ map userId và campaignId
    private Long userId;
    private Long campaignId;
}