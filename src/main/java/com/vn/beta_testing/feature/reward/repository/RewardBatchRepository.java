package com.vn.beta_testing.feature.reward.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.RewardBatch;

public interface RewardBatchRepository extends JpaRepository<RewardBatch, Long> {
    List<RewardBatch> findByCompanyId(Long companyId);

    List<RewardBatch> findByCampaignId(Long campaignId);

    List<RewardBatch> findByCampaignIdOrderByCreatedAtDesc(Long campaignId);
}