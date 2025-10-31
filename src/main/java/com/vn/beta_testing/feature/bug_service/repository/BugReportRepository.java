package com.vn.beta_testing.feature.bug_service.repository;

import java.time.Instant;
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

    @Query("SELECT MONTH(b.createdAt) AS month, COUNT(b) AS total "
            + "FROM BugReport b WHERE b.tester.id = :userId "
            + "AND b.createdAt >= :fromDate "
            + "GROUP BY MONTH(b.createdAt) ORDER BY MONTH(b.createdAt)")
    List<Object[]> countBugsByMonth(@Param("userId") Long userId, @Param("fromDate") Instant fromDate);

    @Query("SELECT COUNT(b) FROM BugReport b WHERE b.tester.id = :userId")
    Long countBugByUserId(@Param("userId") Long userId);

}
