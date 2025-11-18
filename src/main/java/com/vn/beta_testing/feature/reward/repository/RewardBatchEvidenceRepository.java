package com.vn.beta_testing.feature.reward.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.RewardBatchEvidence;

public interface RewardBatchEvidenceRepository extends JpaRepository<RewardBatchEvidence, Long> {
    List<RewardBatchEvidence> findByRewardBatchId(Long rewardBatchId);
}