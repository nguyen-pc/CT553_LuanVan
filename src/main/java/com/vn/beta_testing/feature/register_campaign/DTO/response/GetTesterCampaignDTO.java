package com.vn.beta_testing.feature.register_campaign.DTO.response;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTesterCampaignDTO {
    private Long id;
    private String status;
    private Instant joinDate;
    private UserTesterProfileDTO user;

}
