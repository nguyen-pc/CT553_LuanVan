package com.vn.beta_testing.feature.auth_service.DTO;

import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TesterStatisticSummaryDTO {
    private Long userId;
    private Long totalCampaigns;
    private Long totalBugs;
    private Long totalSurveys;
    private Double totalRewards;
    private Double engagementScore;

    // 🆕 Trend fields
    private List<Long> bugTrend;
    private List<Long> campaignTrend;
    private List<Long> surveyTrend;
}
