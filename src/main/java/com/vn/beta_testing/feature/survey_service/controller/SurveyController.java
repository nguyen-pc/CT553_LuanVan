package com.vn.beta_testing.feature.survey_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.survey_service.DTO.SurveyDTO;
import com.vn.beta_testing.feature.survey_service.mapper.SurveyMapper;
import com.vn.beta_testing.feature.survey_service.service.SurveyService;
import com.vn.beta_testing.util.SecurityUtil;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SurveyController {
    private final SurveyService surveyService;
    private final CampaignService campaignService;
    private final SecurityUtil securityUtil;

    public SurveyController(SurveyService surveyService, CampaignService campaignService, SecurityUtil securityUtil) {
        this.surveyService = surveyService;
        this.campaignService = campaignService;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/campaign/{id}/survey/{surveyId}")
    @ApiMessage("Get survey by id")
    public ResponseEntity<Survey> getSurvey(@PathVariable("id") long id, @PathVariable("surveyId") long surveyId) {
        Survey survey = surveyService.getSurveyById(surveyId);
        return ResponseEntity.ok(survey);
    }

    @GetMapping("/campaign/{id}/survey")
    @ApiMessage("Get surveys by campaign id")
    public ResponseEntity<List<SurveyDTO>> getSurveys(@PathVariable("id") long id) {
        List<Survey> surveys = surveyService.getSurveysByCampaignId(id);
        if (surveys == null) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("Surveys fetched: " + surveys);

        List<SurveyDTO> dtos = SurveyMapper.toDTOList(surveys);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/campaign/{id}/survey")
    public ResponseEntity<Survey> createSurvey(@PathVariable("id") long id,
            @Valid @RequestBody Survey survey)
            throws IdInvalidException {

        Campaign campaign = campaignService.fetchCampaignById(id);
        survey.setCampaign(campaign);
        Survey resSurvey = surveyService.createSurvey(survey);

        return ResponseEntity.status(HttpStatus.CREATED).body(resSurvey);
    }

    @PutMapping("/campaign/{id}/survey")
    public ResponseEntity<Survey> updateSurvey(@PathVariable("id") long id, @RequestBody Survey survey)
            throws IdInvalidException {

        // Campaign campaign = campaignService.fetchCampaignById(id);
        // survey.setCampaign(campaign);
        Survey resSurvey = surveyService.updateSurvey(survey);

        return ResponseEntity.status(HttpStatus.OK).body(resSurvey);
    }

    // @DeleteMapping("/campaign/{id}/survey/{surveyId}")
    // public ResponseEntity<Void> deleteSurvey(@PathVariable("id") long id, @PathVariable("surveyId") long surveyId) {
    //     this.surveyService.deleteSurvey(surveyId);
    //     return ResponseEntity.ok(null);
    // }

    @DeleteMapping("/campaign/{id}/survey/{surveyId}")
    @ApiMessage("Soft delete survey")
    public ResponseEntity<Void> deleteSurvey(
            @PathVariable("id") long id,
            @PathVariable("surveyId") long surveyId) {

        surveyService.softDeleteSurvey(surveyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/campaign/{id}/survey/{surveyId}")
    @ApiMessage("Update survey by id")
    public ResponseEntity<Survey> updateSurvey(
            @PathVariable("id") long campaignId,
            @PathVariable("surveyId") long surveyId,
            @Valid @RequestBody Survey survey) throws IdInvalidException {
        Campaign campaign = campaignService.fetchCampaignById(campaignId);
        if (campaign == null) {
            throw new IdInvalidException("Campaign with id = " + campaignId + " does not exist.");
        }

        survey.setCampaign(campaign);
        survey.setSurveyId(surveyId);

        Survey updatedSurvey = surveyService.updateSurvey(survey);

        return ResponseEntity.status(HttpStatus.OK).body(updatedSurvey);
    }

}
