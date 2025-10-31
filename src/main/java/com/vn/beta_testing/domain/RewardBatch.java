package com.vn.beta_testing.domain;

import java.time.Instant;

import com.vn.beta_testing.util.constant.BatchStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reward_batches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;
    private String batchCode;
    private Integer totalUsers;
    private Double totalAmount;
    private String createdBy;

    @Enumerated(EnumType.STRING)
    private BatchStatus status = BatchStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String paymentUrl; // link thanh toán VNPAY

    private String transactionNo; // mã giao dịch VNPAY
    private String vnpTxnRef; // mã tham chiếu VNPAY
    private String vnpResponseCode;
    private Instant createdAt = Instant.now();
    private Instant paidAt;
}