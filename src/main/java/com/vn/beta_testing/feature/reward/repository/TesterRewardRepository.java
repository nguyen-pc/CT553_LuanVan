package com.vn.beta_testing.feature.reward.repository;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.vn.beta_testing.domain.TesterReward;
import com.vn.beta_testing.util.constant.RewardStatus;

@Repository
public interface TesterRewardRepository extends JpaRepository<TesterReward, Long> {

    List<TesterReward> findByStatus(RewardStatus status);

    @Query("SELECT COUNT(r) FROM TesterReward r WHERE r.status = 'APPROVED'")
    long countApproved();

    @Query("SELECT COUNT(r) FROM TesterReward r WHERE r.status = 'REJECTED'")
    long countRejected();

    @Query("SELECT COUNT(r) FROM TesterReward r")
    long countAllRewards();

    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, r.requestedAt, r.verifiedAt)) FROM TesterReward r WHERE r.status IN ('VERIFIED','APPROVED','TRANSFERRED')")
    Double avgVerificationTime();

    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, r.approvedAt, r.transferredAt)) FROM TesterReward r WHERE r.status IN ('TRANSFERRED','CONFIRMED')")
    Double avgTransferTime();

    @Query("SELECT SUM(r.amount) FROM TesterReward r WHERE MONTH(r.confirmedAt) = MONTH(:now) AND YEAR(r.confirmedAt) = YEAR(:now)")
    Double sumRewardCurrentMonth(@Param("now") Instant now);

    @Query("SELECT FUNCTION('DATE_FORMAT', r.confirmedAt, '%Y-%m') as month, SUM(r.amount) FROM TesterReward r GROUP BY FUNCTION('DATE_FORMAT', r.confirmedAt, '%Y-%m')")
    List<Object[]> sumRewardByMonth();

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM TesterReward r WHERE r.userId = :userId AND r.status = 'APPROVED'")
    Double sumRewardsByUserId(@Param("userId") Long userId);

}
