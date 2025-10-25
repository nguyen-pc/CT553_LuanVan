package com.vn.beta_testing.feature.email_service.DTO;

import java.time.Instant;
import com.vn.beta_testing.util.constant.EmailStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTesterDTO {
    private Long id;
    private String email;
    private EmailStatus status;
    private Instant sentAt;
    private Long campaignId;
    private String campaignTitle;
    private Instant createdAt;
}