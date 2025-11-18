package com.vn.beta_testing.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vn.beta_testing.util.constant.RewardBatchStatus;
import com.vn.beta_testing.util.constant.RewardEvidenceType;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "reward_batch_evidence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardBatchEvidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reward_batch_id")
    private Long rewardBatchId;

    @Enumerated(EnumType.STRING)
    private RewardEvidenceType type;

    private String fileUrl;
    private String fileName;
    private String note;

    @Column(name = "uploaded_by")
    private Long uploadedById;

    private LocalDateTime uploadedAt = LocalDateTime.now();
}
