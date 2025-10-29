package com.vn.beta_testing.feature.register_campaign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.TesterCampaign;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterStatisticDTO;
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

  @Query(value = """
          SELECT DATE(t.completionDate) AS date, COUNT(t.id) AS completedCount
          FROM TesterCampaign t
          WHERE t.completionDate IS NOT NULL
            AND t.campaign.id = :campaignId
          GROUP BY DATE(t.completionDate)
          ORDER BY DATE(t.completionDate)
      """)
  List<Object[]> getCompletionStatsByDate(@Param("campaignId") Long campaignId);

  @Query("""
          SELECT new com.vn.beta_testing.feature.register_campaign.DTO.response.TesterStatisticDTO(
              u.id,
              c.id,
              tc.id,
              u.name,
              COALESCE(c.rewardValue, '0'),
              0.0,
               COALESCE(AVG(CASE WHEN te.status = true THEN 1 ELSE 0 END), 0),
                tc.progress
          )
          FROM TesterCampaign tc
          JOIN tc.user u
          JOIN tc.campaign c
          LEFT JOIN TestExecution te ON te.user = u AND te.campaign.id = c.id
          WHERE c.id = :campaignId
          GROUP BY u.id, c.id, tc.id, u.name, c.rewardValue, tc.progress
      """)
  List<TesterStatisticDTO> getTesterBasicStats(@Param("campaignId") Long campaignId);

}