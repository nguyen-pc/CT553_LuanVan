package com.vn.beta_testing.feature.register_campaign.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TesterStatisticDTO {
    private String testerName;
    private String reward;
    private Double bonus;
    private Double engagementScore;
    private Long userId;
    private Long campaignId;
    private Long testerCampaignId;
    private Integer progress;

    // các thống kê mở rộng (sẽ gán trong Service)
    private String surveysCompleted;
    private Long bugsSubmitted;
    private Long messages;

    public TesterStatisticDTO(Long userId, Long campaignId, Long testerCampaignId, String testerName, String reward,
            Double bonus,
            Double engagementScore, Integer progress) {
        this.userId = userId;
        this.campaignId = campaignId;
        this.testerCampaignId = testerCampaignId;
        this.testerName = testerName;
        this.reward = reward;
        this.bonus = bonus;
        this.engagementScore = engagementScore;
        this.progress = progress;
    }
}