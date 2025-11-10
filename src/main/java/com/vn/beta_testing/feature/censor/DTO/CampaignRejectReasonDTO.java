package com.vn.beta_testing.feature.censor.DTO;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignRejectReasonDTO {
    private Long id;
    private String initialReason;
    private Long campaignId;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private List<CampaignRejectMessageDTO> messages;
}