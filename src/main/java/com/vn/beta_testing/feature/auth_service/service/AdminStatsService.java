package com.vn.beta_testing.feature.auth_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.feature.auth_service.DTO.AdminDashboardDTO;
import com.vn.beta_testing.feature.auth_service.DTO.TrendPoint;
import com.vn.beta_testing.feature.auth_service.repository.UserRepository;
import com.vn.beta_testing.feature.company_service.repository.CampaignRepository;
import com.vn.beta_testing.feature.company_service.repository.CompanyRepository;
import com.vn.beta_testing.feature.company_service.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminStatsService {

    private final UserRepository userRepo;
    private final CompanyRepository companyRepo;
    private final CampaignRepository campaignRepo;
    private final ProjectRepository projectRepo;

    public AdminStatsService(UserRepository userRepo, CompanyRepository companyRepo,
            CampaignRepository campaignRepo, ProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
        this.campaignRepo = campaignRepo;
        this.projectRepo = projectRepo;
    }

    public AdminDashboardDTO getOverviewStats() {
        long totalUsers = userRepo.countAllUsers();
        long totalCompanies = companyRepo.countAllCompanies();
        long totalCampaigns = campaignRepo.countAllCampaigns();
        long totalProjects = projectRepo.countAllProjects();

        // tăng trưởng
        double userGrowth = calcGrowth(userRepo.countUsersLast30Days(), userRepo.countUsersPrev30Days());
        double companyGrowth = calcGrowth(companyRepo.countCompaniesLast30Days(),
                companyRepo.countCompaniesPrev30Days());
        double campaignGrowth = calcGrowth(campaignRepo.countCampaignsLast30Days(),
                campaignRepo.countCampaignsPrev30Days());
        double projectGrowth = calcGrowth(projectRepo.countProjectsLast30Days(), projectRepo.countProjectsPrev30Days());

        // trend có ngày
        List<TrendPoint> userTrend = convertToTrend(userRepo.findUserTrendLast30Days());
        List<TrendPoint> companyTrend = convertToTrend(companyRepo.findCompanyTrendLast30Days());
        List<TrendPoint> campaignTrend = convertToTrend(campaignRepo.findCampaignTrendLast30Days());
        List<TrendPoint> projectTrend = convertToTrend(projectRepo.findProjectTrendLast30Days());

        return AdminDashboardDTO.builder()
                .totalUsers(totalUsers)
                .totalCompanies(totalCompanies)
                .totalCampaigns(totalCampaigns)
                .totalProjects(totalProjects)
                .userGrowthRate(userGrowth)
                .companyGrowthRate(companyGrowth)
                .campaignGrowthRate(campaignGrowth)
                .projectGrowthRate(projectGrowth)
                .userTrend(userTrend)
                .companyTrend(companyTrend)
                .campaignTrend(campaignTrend)
                .projectTrend(projectTrend)
                .build();
    }

    private double calcGrowth(long current, long previous) {
        if (previous == 0)
            return current > 0 ? 100.0 : 0.0;
        return ((double) current / previous - 1.0) * 100.0;
    }

    private List<TrendPoint> convertToTrend(List<Object[]> raw) {
        return raw.stream()
                .map(o -> new TrendPoint(o[0].toString(), ((Number) o[1]).intValue()))
                .collect(Collectors.toList());
    }
}