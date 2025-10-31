package com.vn.beta_testing.feature.reward.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.RewardBatch;

@Repository
public interface RewardBatchRepository extends JpaRepository<RewardBatch, Long> {
}