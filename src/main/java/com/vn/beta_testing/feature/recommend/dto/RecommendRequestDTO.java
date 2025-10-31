package com.vn.beta_testing.feature.recommend.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecommendRequestDTO {
    private UserAudienceDTO user;
    private List<CampaignProfileDTO> campaigns;
}
