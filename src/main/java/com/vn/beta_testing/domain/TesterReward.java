package com.vn.beta_testing.domain;

import java.time.Instant;
import com.vn.beta_testing.util.constant.RewardStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tester_rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TesterReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long testerCampaignId;
    private Long userId;
    private Long campaignId;

    private Double amount;
    private String proofUrl;

    @Enumerated(EnumType.STRING)
    private RewardStatus status = RewardStatus.PENDING;

    private Instant requestedAt;
    private Instant verifiedAt;
    private Instant approvedAt;
    private Instant transferredAt;
    private Instant confirmedAt;

    private Boolean adminSlaViolated = false;
    private Boolean companySlaViolated = false;

    private String verifiedBy;
    private String approvedBy;
    private String confirmedBy;
}