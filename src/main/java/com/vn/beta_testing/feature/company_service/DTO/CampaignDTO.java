package com.vn.beta_testing.feature.company_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDTO {

    private Long id;
    private String title;
    private String description;

    private Instant startDate;
    private Instant endDate;

    private String status;
    private Boolean isPublic;

    private String rewardType;
    private String rewardValue;

    private String campaignTypeName;
    private String createdBy;
}