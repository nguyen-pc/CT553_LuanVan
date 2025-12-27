package com.vn.beta_testing.feature.survey_service.service;

import java.lang.StackWalker.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.survey_service.repository.SurveyRepository;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final CampaignService campaignService;

    public SurveyService(SurveyRepository surveyRepository, CampaignService campaignService) {
        this.surveyRepository = surveyRepository;
        this.campaignService = campaignService;
    }

    public Survey createSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    public Survey getSurveyById(long surveyId) {
        return surveyRepository.findById(surveyId).orElse(null);
    }

    public Survey updateSurvey(Survey survey) {
        Optional<Survey> currentSurveyOptional = surveyRepository.findById(survey.getSurveyId());
        if (currentSurveyOptional.isEmpty()) {
            throw new IdInvalidException("Survey with id = " + survey.getSurveyId() + " does not exist.");
        }

        Survey currentSurvey = currentSurveyOptional.get();
        currentSurvey.setSurveyName(survey.getSurveyName());
        currentSurvey.setDescription(survey.getDescription());
        currentSurvey.setStartDate(survey.getStartDate());
        currentSurvey.setEndDate(survey.getEndDate());
        currentSurvey.setUpdatedAt(survey.getUpdatedAt());
        currentSurvey.setUpdatedBy(survey.getUpdatedBy());

        return this.surveyRepository.save(currentSurvey);
    }

    public void deleteSurvey(long surveyId) {
        surveyRepository.deleteById(surveyId);
    }

    public void softDeleteSurvey(long surveyId) {
        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
        if (optionalSurvey.isEmpty()) {
            throw new IdInvalidException("Survey with id = " + surveyId + " does not exist.");
        }

        Survey survey = optionalSurvey.get();
        survey.setDeleted(true); // đánh dấu đã xóa
        survey.setUpdatedAt(Instant.now()); // cập nhật thời gian
        surveyRepository.save(survey);
    }

    public List<Survey> getSurveysByCampaignId(long campaignId) {
        Campaign campaign = campaignService.fetchCampaignById(campaignId);
        if (campaign == null) {
            return null;
        }
        return surveyRepository.findByCampaign_IdAndIsDeletedFalse(campaign.getId());
    }
}
