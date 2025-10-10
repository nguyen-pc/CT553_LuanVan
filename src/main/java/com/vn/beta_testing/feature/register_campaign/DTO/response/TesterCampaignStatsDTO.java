package com.vn.beta_testing.feature.register_campaign.DTO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TesterCampaignStatsDTO {
    private long accepted;
    private long rejected;
    private long pending;
    private long applied;
    private long invited;
}
