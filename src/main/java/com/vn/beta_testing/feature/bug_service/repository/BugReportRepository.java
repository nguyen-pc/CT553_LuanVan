package com.vn.beta_testing.feature.bug_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vn.beta_testing.domain.BugReport;

public interface BugReportRepository extends JpaRepository<BugReport, Long>, JpaSpecificationExecutor<BugReport> {
    List<BugReport> findByTesterId(Long testerId);

    List<BugReport> findByTester_Id(Long testerId);

    List<BugReport> findByAssignee_Id(Long assigneeId);

    List<BugReport> findByBugType_Id(Long bugTypeId);

    List<BugReport> findByCampaign_Id(Long campaignId);

    @Query(value = """
                SELECT DATE(b.createdAt) AS date, b.severity AS severity, COUNT(b.id) AS count
                FROM BugReport b
                WHERE b.campaign.id = :campaignId
                GROUP BY DATE(b.createdAt), b.severity
                ORDER BY DATE(b.createdAt)
            """)
    List<Object[]> getBugCountBySeverityAndDate(@Param("campaignId") Long campaignId);

    @Query("""
                SELECT COUNT(b)
                FROM BugReport b
                WHERE b.createdBy = :name
                AND b.campaign.id = :campaignId
            """)
    Long countByCreatedByAndCampaignId(@Param("name") String name, @Param("campaignId") Long campaignId);
}
