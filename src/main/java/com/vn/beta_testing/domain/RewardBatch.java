package com.vn.beta_testing.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.vn.beta_testing.util.constant.RewardBatchStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reward_batch")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // thay vì @ManyToOne → dùng ID
    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Enumerated(EnumType.STRING)
    private RewardBatchStatus status = RewardBatchStatus.DRAFT;

    private BigDecimal totalAmount;

    private String note;

    @Column(name = "approved_by")
    private Long approvedById; // chỉ lưu id

    @Column(name = "finalized_by")
    private Long finalizedById;

    private LocalDateTime finalizedAt;

    private LocalDateTime approvedAt;

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
