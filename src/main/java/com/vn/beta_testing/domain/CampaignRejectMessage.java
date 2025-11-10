package com.vn.beta_testing.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vn.beta_testing.util.SecurityUtil;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "campaign_reject_messages")
@Getter
@Setter
public class CampaignRejectMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reject_reason_id")
    @JsonBackReference
    private CampaignRejectReason rejectReason;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = Instant.now();
    }
}
