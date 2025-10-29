package com.vn.beta_testing.feature.survey_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long>, JpaSpecificationExecutor<Survey> {
    List<Survey> findByCampaign_Id(long campaignId);

    @Query("SELECT COUNT(s) FROM Survey s WHERE s.campaign.id = :campaignId")
    int countByCampaignId(@Param("campaignId") Long campaignId);
}