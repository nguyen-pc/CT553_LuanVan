package com.vn.beta_testing.feature.test_execution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vn.beta_testing.domain.TestExecution;

@Repository
public interface TestExecutionRepository extends JpaRepository<TestExecution, Long> {

    List<TestExecution> findByCampaignId(Long campaignId);

    List<TestExecution> findByUserId(Long userId);

    List<TestExecution> findByTestCaseId(Long testCaseId);

    List<TestExecution> findByCampaignIdAndUserId(Long campaignId, Long userId);

    List<TestExecution> findByCampaignIdAndTestCaseId(Long campaignId, Long testCaseId);

    Optional<TestExecution> findByCampaignIdAndUserIdAndTestCaseId(Long campaignId, Long userId, Long testCaseId);
}