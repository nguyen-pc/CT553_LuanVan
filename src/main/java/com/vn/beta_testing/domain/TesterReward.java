package com.vn.beta_testing.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vn.beta_testing.util.constant.RewardBatchStatus;
import com.vn.beta_testing.util.constant.RewardEvidenceType;
import com.vn.beta_testing.util.constant.TesterRewardStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tester_reward")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TesterReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reward_batch_id", nullable = false)
    private Long rewardBatchId;

    @Column(name = "tester_id", nullable = false)
    private Long testerId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TesterRewardStatus status = TesterRewardStatus.PENDING;

    private String failureReason;
    private String evidenceUrl;

    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
