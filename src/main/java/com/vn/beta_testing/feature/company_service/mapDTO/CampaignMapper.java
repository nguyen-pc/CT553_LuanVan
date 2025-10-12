package com.vn.beta_testing.feature.company_service.mapDTO;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.feature.company_service.DTO.CampaignDTO;


public class CampaignMapper {

    public static CampaignDTO toDTO(Campaign c) {
        if (c == null) return null;

        return CampaignDTO.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .status(c.getStatus())
                .isPublic(c.getIsPublic())
                .rewardType(c.getRewardType())
                .rewardValue(c.getRewardValue())
                .campaignTypeName(c.getCampaignType() != null ? c.getCampaignType().getName() : null)
                .createdBy(c.getCreatedBy())
                .build();
    }
}