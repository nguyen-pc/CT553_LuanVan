package com.vn.beta_testing.feature.censor.DTO;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignRejectMessageDTO {
    private Long id;
    private String content;
    private Long senderId;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}