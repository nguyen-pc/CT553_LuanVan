package com.vn.beta_testing.feature.company_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long>, JpaSpecificationExecutor<Campaign> {
    // Admin Dashboard Queries

    @Query(value = "SELECT COUNT(*) FROM campaigns", nativeQuery = true)
    long countAllCampaigns();

    @Query(value = "SELECT COUNT(*) FROM campaigns WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)", nativeQuery = true)
    long countCampaignsLast30Days();

    @Query(value = """
        SELECT COUNT(*) FROM campaigns
        WHERE created_at BETWEEN DATE_SUB(CURDATE(), INTERVAL 60 DAY) AND DATE_SUB(CURDATE(), INTERVAL 30 DAY)
        """, nativeQuery = true)
    long countCampaignsPrev30Days();

    @Query(value = """
        SELECT DATE(created_at) AS date, COUNT(*) AS count
        FROM campaigns
        WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
        GROUP BY DATE(created_at)
        ORDER BY DATE(created_at)
    """, nativeQuery = true)
    List<Object[]> findCampaignTrendLast30Days();
}