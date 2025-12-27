package com.vn.beta_testing.feature.survey_service.mapper;

import java.util.List;
import java.util.stream.Collectors;
import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.feature.survey_service.DTO.SurveyDTO;

public class SurveyMapper {
    public static SurveyDTO toDTO(Survey s) {
        SurveyDTO dto = new SurveyDTO();
        dto.setSurveyId(s.getSurveyId());
        dto.setSurveyName(s.getSurveyName());
        dto.setSubTitle(s.getSubTitle());
        dto.setDescription(s.getDescription());
        dto.setDeleted(s.isDeleted());
        dto.setStartDate(s.getStartDate());
        dto.setEndDate(s.getEndDate());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setUpdatedAt(s.getUpdatedAt());
        dto.setCreatedBy(s.getCreatedBy());
        dto.setUpdatedBy(s.getUpdatedBy());
        return dto;
    }

    public static List<SurveyDTO> toDTOList(List<Survey> surveys) {
        return surveys.stream().map(SurveyMapper::toDTO).collect(Collectors.toList());
    }
}
