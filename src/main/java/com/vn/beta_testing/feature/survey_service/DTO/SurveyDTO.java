package com.vn.beta_testing.feature.survey_service.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SurveyDTO {
    private Long surveyId;
    private String surveyName;
    private String subTitle;
    private String description;
    private boolean isDeleted;
    private Instant startDate;
    private Instant endDate;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}