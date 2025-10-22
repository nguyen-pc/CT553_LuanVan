package com.vn.beta_testing.feature.survey_service.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionResponseDTO {
    private Long questionId;
    private String questionName;
    private String questionType;
    private List<AnswerDetailDTO> answers;
}

