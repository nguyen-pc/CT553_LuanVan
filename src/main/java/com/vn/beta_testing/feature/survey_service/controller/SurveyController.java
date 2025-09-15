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

import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.feature.company_service.service.ProjectService;
import com.vn.beta_testing.feature.survey_service.service.SurveyService;
import com.vn.beta_testing.util.SecurityUtil;

@Controller
public class SurveyController {
    private final SurveyService surveyService;
    private final ProjectService projectService;
    private final SecurityUtil securityUtil;

    public SurveyController(SurveyService surveyService, ProjectService projectService, SecurityUtil securityUtil) {
        this.surveyService = surveyService;
        this.projectService = projectService;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/project/campaign/{id}/survey/{surveyId}")
    public ResponseEntity<Survey> getSurvey(@PathVariable("id") long id, @PathVariable("surveyId") long surveyId) {
        Survey survey = surveyService.getSurveyById(surveyId);
        return ResponseEntity.ok(survey);
    }
}
