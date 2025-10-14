package com.vn.beta_testing.feature.test_execution.DTO;

import java.time.Instant;
import lombok.Data;

@Data
public class TestExecutionDTO {
    private Long id;
    private String note;
    private boolean status;
    private Instant createdAt;
    private Instant updatedAt;

    private Long campaignId;
    private String campaignTitle;

    private Long userId;
    private String userEmail;

    private Long testCaseId;
    private String testCaseTitle;
}