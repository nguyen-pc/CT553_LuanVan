package com.vn.beta_testing.feature.test_execution.DTO;

import java.time.Instant;

import com.vn.beta_testing.domain.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TesterResponseDTO {
    private Long id;

    private Boolean completed;
    private Instant completionDate;

    // User summary
    private Long userId;
    private String username;
    private String email;

    // Survey summary
    private Long surveyId;
    private String surveyTitle;

    // Response (giữ nguyên) + id
    private Long responseId;
    private Response response;
}