package com.vn.beta_testing.feature.auth_service.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.feature.auth_service.DTO.TesterStatisticSummaryDTO;
import com.vn.beta_testing.feature.bug_service.repository.BugReportRepository;
import com.vn.beta_testing.feature.register_campaign.repository.TesterCampaignRepository;
// import com.vn.beta_testing.feature.reward.repository.TesterRewardRepository;
import com.vn.beta_testing.feature.test_execution.repository.TesterSurveyRepository;

@Service
@Transactional(readOnly = true)
public class TesterStatisticService {
    private final TesterCampaignRepository testerCampaignRepo;
    private final BugReportRepository bugReportRepo;
    private final TesterSurveyRepository testerSurveyRepo;
    // private final TesterRewardRepository testerRewardRepo;

    public TesterStatisticService(
            TesterCampaignRepository testerCampaignRepo,
            BugReportRepository bugReportRepo,
            TesterSurveyRepository testerSurveyRepo
            // TesterRewardRepository testerRewardRepo
            ) {
        this.testerCampaignRepo = testerCampaignRepo;
        this.bugReportRepo = bugReportRepo;
        this.testerSurveyRepo = testerSurveyRepo;
        // this.testerRewardRepo = testerRewardRepo;
    }

    public TesterStatisticSummaryDTO getTesterStatistics(Long userId) {
        Long totalCampaigns = testerCampaignRepo.countByUserId(userId);
        Long totalBugs = bugReportRepo.countBugByUserId(userId);
        Long totalSurveys = testerSurveyRepo.countCompletedSurvey(userId);
        // Double totalRewards = testerRewardRepo.sumRewardsByUserId(userId);

        Double engagementScore = totalCampaigns > 0
                ? ((double) (totalBugs + totalSurveys) / totalCampaigns)
                : 0.0;

        // 🧭 6 tháng gần nhất
        Instant fromDate = LocalDate.now().minusMonths(6)
                .atStartOfDay(ZoneId.systemDefault()).toInstant();

        // 🔁 Xử lý dữ liệu dạng [tháng, count]
        List<Long> bugTrend = convertToMonthlyTrend(bugReportRepo.countBugsByMonth(userId, fromDate));
        List<Long> campaignTrend = convertToMonthlyTrend(testerCampaignRepo.countCampaignsByMonth(userId, fromDate));
        List<Long> surveyTrend = convertToMonthlyTrend(testerSurveyRepo.countSurveysByMonth(userId, fromDate));

        return TesterStatisticSummaryDTO.builder()
                .userId(userId)
                .totalCampaigns(totalCampaigns)
                .totalBugs(totalBugs)
                .totalSurveys(totalSurveys)
                // .totalRewards(totalRewards)
                .engagementScore(engagementScore)
                .bugTrend(bugTrend)
                .campaignTrend(campaignTrend)
                .surveyTrend(surveyTrend)
                .build();
    }

    private List<Long> convertToMonthlyTrend(List<Object[]> result) {
        Map<Integer, Long> map = new HashMap<>();
        result.forEach(r -> map.put(((Number) r[0]).intValue(), ((Number) r[1]).longValue()));

        // đảm bảo trả về đủ 6 tháng gần nhất, kể cả tháng không có dữ liệu
        List<Long> trend = new ArrayList<>();
        int currentMonth = LocalDate.now().getMonthValue();
        for (int i = 5; i >= 0; i--) {
            int month = (currentMonth - i + 12) % 12;
            month = (month == 0) ? 12 : month;
            trend.add(map.getOrDefault(month, 0L));
        }
        return trend;
    }
}