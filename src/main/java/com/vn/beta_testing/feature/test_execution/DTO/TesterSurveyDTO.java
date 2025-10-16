package com.vn.beta_testing.feature.test_execution.DTO;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TesterSurveyDTO {
    private Long id;
    private Boolean completed;
    private Instant completionDate;
    private Long userId;
    private Long surveyId;
    private Long responseId;
}