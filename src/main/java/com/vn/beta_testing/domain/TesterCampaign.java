package com.vn.beta_testing.domain;

import java.time.Instant;

import com.vn.beta_testing.util.constant.TesterCampaignStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tester_campaign")
@Getter
@Setter
public class TesterCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TesterCampaignStatus status = TesterCampaignStatus.PENDING; // ✅ mặc định là PENDING

    private Integer progress;
    private String roleInCampaign;
    private Instant joinDate = Instant.now();
    private Instant completionDate;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
}