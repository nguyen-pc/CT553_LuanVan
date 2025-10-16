package com.vn.beta_testing.feature.bug_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vn.beta_testing.domain.BugReport;

public interface BugReportRepository extends JpaRepository<BugReport, Long>, JpaSpecificationExecutor<BugReport> {
    List<BugReport> findByTesterId(Long testerId);

    List<BugReport> findByTester_Id(Long testerId);

    List<BugReport> findByAssignee_Id(Long assigneeId);

    List<BugReport> findByBugType_Id(Long bugTypeId);

    List<BugReport> findByCampaign_Id(Long campaignId);
}
