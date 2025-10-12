package com.vn.beta_testing.feature.register_campaign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.TesterCampaign;
import com.vn.beta_testing.util.constant.TesterCampaignStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TesterCampaignRepository
        extends JpaRepository<TesterCampaign, Long>, JpaSpecificationExecutor<TesterCampaign> {

    List<TesterCampaign> findByCampaign_Id(Long campaignId);

    List<TesterCampaign> findByUser_Id(Long userId);


    Optional<TesterCampaign> findByUserIdAndCampaignId(Long userId, Long campaignId);

    boolean existsByUser_IdAndCampaign_Id(Long userId, Long campaignId);

    long countByCampaignIdAndStatus(Long campaignId, TesterCampaignStatus status);

    long countByCampaignId(Long campaignId); // tổng số tester (được xem như "applied")

}