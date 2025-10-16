package com.vn.beta_testing.feature.test_execution.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.TesterSurvey;
import com.vn.beta_testing.domain.Response;
import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.auth_service.repository.UserRepository;
import com.vn.beta_testing.feature.survey_service.repository.ResponseRepository;
import com.vn.beta_testing.feature.survey_service.repository.SurveyRepository;
import com.vn.beta_testing.feature.test_execution.DTO.TesterSurveyDTO;
import com.vn.beta_testing.feature.test_execution.repository.TesterSurveyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TesterSurveyService {

    private final TesterSurveyRepository testerSurveyRepository;
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final ResponseRepository responseRepository;

    public TesterSurveyDTO create(TesterSurveyDTO dto) {
        TesterSurvey entity = new TesterSurvey();
        entity.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);
        entity.setCompletionDate(dto.getCompletionDate() != null ? dto.getCompletionDate() : Instant.now());

        if (dto.getUserId() != null)
            entity.setUser(userRepository.findById(dto.getUserId()).orElse(null));

        if (dto.getSurveyId() != null)
            entity.setSurvey(surveyRepository.findById(dto.getSurveyId()).orElse(null));

        if (dto.getResponseId() != null)
            entity.setResponse(responseRepository.findById(dto.getResponseId()).orElse(null));

        testerSurveyRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCompletionDate(entity.getCompletionDate());
        return dto;
    }

    public List<TesterSurveyDTO> getAll() {
        return testerSurveyRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TesterSurveyDTO> getById(Long id) {
        return testerSurveyRepository.findById(id).map(this::toDTO);
    }

    public void delete(Long id) {
        testerSurveyRepository.deleteById(id);
    }

    private TesterSurveyDTO toDTO(TesterSurvey e) {
        TesterSurveyDTO dto = new TesterSurveyDTO();
        dto.setId(e.getId());
        dto.setCompleted(e.getCompleted());
        dto.setCompletionDate(e.getCompletionDate());
        dto.setUserId(e.getUser() != null ? e.getUser().getId() : null);
        dto.setSurveyId(e.getSurvey() != null ? e.getSurvey().getSurveyId() : null);
        dto.setResponseId(e.getResponse() != null ? e.getResponse().getResponseId() : null);
        return dto;
    }
}