package com.vn.beta_testing.feature.reward.DTO;

import com.vn.beta_testing.util.constant.RewardEvidenceType;
import lombok.Data;

@Data
public class UploadEvidenceRequest {

    private RewardEvidenceType type;
    private String fileUrl;
    private String fileName;
    private String note;
}