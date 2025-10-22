package com.vn.beta_testing.feature.survey_service.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AnswerDetailDTO {
    private Long answerId;
    private String choiceText;
    private String answerText; // nếu là text
    private Long responseId;
    private LocalDateTime submittedAt;
    private String userEmail;
}