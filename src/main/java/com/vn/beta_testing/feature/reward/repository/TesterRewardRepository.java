package com.vn.beta_testing.feature.reward.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.TesterReward;

public interface TesterRewardRepository extends JpaRepository<TesterReward, Long> {
    List<TesterReward> findByRewardBatchId(Long rewardBatchId);
    List<TesterReward> findByTesterId(Long testerId);
}