package com.vn.beta_testing.feature.auth_service.DTO;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardDTO {
    private long totalUsers;
    private long totalCompanies;
    private long totalCampaigns;
    private long totalProjects;

    private double userGrowthRate;
    private double companyGrowthRate;
    private double campaignGrowthRate;
    private double projectGrowthRate;

    private List<TrendPoint> userTrend;
    private List<TrendPoint> companyTrend;
    private List<TrendPoint> campaignTrend;
    private List<TrendPoint> projectTrend;
}