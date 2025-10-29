package com.vn.beta_testing.feature.register_campaign.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.feature.bug_service.repository.BugReportRepository;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterStatisticDTO;
import com.vn.beta_testing.feature.register_campaign.repository.TesterCampaignRepository;
import com.vn.beta_testing.feature.survey_service.repository.SurveyRepository;
import com.vn.beta_testing.feature.test_execution.repository.TesterSurveyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TesterAnalyticsService {

    private final TesterCampaignRepository testerCampaignRepository;
    private final SurveyRepository surveyRepository;
    private final TesterSurveyRepository testerSurveyRepository;
    private final BugReportRepository bugReportRepository;
    public List<TesterStatisticDTO> getTesterStatistics(Long campaignId) {
        // 1️⃣ Lấy thống kê cơ bản
        List<TesterStatisticDTO> testers = testerCampaignRepository.getTesterBasicStats(campaignId);

        // 2️⃣ Tính tổng số survey của campaign (dùng 1 lần)
        int totalSurveys = surveyRepository.countByCampaignId(campaignId);

        // 3️⃣ Duyệt từng tester và tính thêm thống kê
        testers.forEach(t -> {
            Long bugs = bugReportRepository.countByCreatedByAndCampaignId(t.getTesterName(), campaignId);
            int completed = testerSurveyRepository.countByNameAndCampaignId(t.getTesterName(), campaignId);

            t.setBugsSubmitted(bugs);
            t.setSurveysCompleted(completed + " / " + totalSurveys);
        });

        return testers;
    }
}