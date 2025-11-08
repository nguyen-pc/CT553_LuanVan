package com.vn.beta_testing.feature.company_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.feature.company_service.repository.DashboardRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public Map<String, Object> getDashboardOverview(String type, Long companyId) {
        boolean isCompany = "company".equalsIgnoreCase(type);
        Long filterCompanyId = isCompany ? companyId : null;

        Map<String, Object> data = new HashMap<>();

        data.put("totalUsers", dashboardRepository.countUsers(filterCompanyId));
        data.put("totalCompanies", dashboardRepository.countCompanies(filterCompanyId));
        data.put("totalProjects", dashboardRepository.countProjects(filterCompanyId));
        data.put("totalCampaigns", dashboardRepository.countCampaigns(filterCompanyId));
        data.put("totalBugs", dashboardRepository.countBugs(companyId));
        
        data.put("bugStatusDistribution", dashboardRepository.countBugsByStatus(companyId));
        data.put("campaignStatusDistribution", dashboardRepository.countCampaignsByStatus(companyId));

        data.put("userTrend", mapTrend(dashboardRepository.findUserTrend(filterCompanyId)));
        data.put("companyTrend", mapTrend(dashboardRepository.findCompanyTrend(filterCompanyId)));
        data.put("projectTrend", mapTrend(dashboardRepository.findProjectTrend(filterCompanyId)));
        data.put("campaignTrend", mapTrend(dashboardRepository.findCampaignTrend(filterCompanyId)));

        // Tính tỷ lệ tăng trưởng (fake logic đơn giản)
        data.put("userGrowthRate", 100.0);
        data.put("companyGrowthRate", 100.0);
        data.put("projectGrowthRate", 100.0);
        data.put("campaignGrowthRate", 100.0);

        return data;
    }

    private List<Map<String, Object>> mapTrend(List<Object[]> results) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("count", row[1]);
            list.add(map);
        }
        return list;
    }
}