package com.vn.beta_testing.feature.register_campaign.DTO.response;

import java.time.Instant;

import com.vn.beta_testing.feature.company_service.DTO.CampaignDTO;
import com.vn.beta_testing.util.constant.TesterCampaignStatus;
import lombok.Getter;
import lombok.Setter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TesterCampaignDTO {
    private Long id;
    private String status;
    private Integer progress;
    private String note;
    private Long userId;
    private Instant joinDate;
    private boolean isUpload;
    private String uploadLink;
    private CampaignDTO campaign; // 👈 Thêm phần này
}