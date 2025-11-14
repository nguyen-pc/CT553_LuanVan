package com.vn.beta_testing.feature.company_service.DTO;

import com.vn.beta_testing.util.constant.RiskLevelEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleResponseDTO {

    private Long id;
    private String moduleName;
    private String description;
    private RiskLevelEnum riskLevel;
    private Long projectId;
    private String projectName;

    private String createdBy;
    private String updatedBy;
}