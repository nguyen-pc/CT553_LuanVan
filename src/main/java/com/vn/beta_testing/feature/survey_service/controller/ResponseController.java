package com.vn.beta_testing.feature.survey_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.Response;
import com.vn.beta_testing.feature.survey_service.service.ResponseService;
import com.vn.beta_testing.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @PostMapping("campaign/{campaignId}/survey/{surveyId}/response")
    @ApiMessage("Create a response for a survey in a campaign")
    public ResponseEntity<?> createResponse(@PathVariable("campaignId") long campaignId,
            @PathVariable("surveyId") long surveyId,
            @RequestBody Response response) {
        try {
            Response savedResponse = this.responseService.createResponse(campaignId, surveyId, response);
            return ResponseEntity.ok(savedResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Internal Error: " + e.getMessage());
        }
    }

    @GetMapping("project/{projectId}/campaign/{campaignId}/survey/{surveyId}/response/{responseId}")
    @ApiMessage("Get a response for a survey in a campaign")
    public ResponseEntity<?> getResponse(@PathVariable("projectId") long projectId,
            @PathVariable("campaignId") long campaignId,
            @PathVariable("surveyId") long surveyId,
            @PathVariable("responseId") long responseId) {
        try {
            Response dbResponse = this.responseService.getResponse(campaignId, surveyId, responseId);
            return ResponseEntity.ok(dbResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Internal error: " + e.getMessage());
        }
    }

    @DeleteMapping("project/{projectId}/campaign/{campaignId}/survey/{surveyId}/response/{responseId}")
    @ApiMessage("Delete a response for a survey in a campaign")
    public ResponseEntity<?> deleteResponse(@PathVariable("projectId") long projectId,
            @PathVariable("campaignId") long campaignId,
            @PathVariable("surveyId") long surveyId,
            @PathVariable("responseId") long responseId) {
        try {
            this.responseService.deleteResponse(campaignId, surveyId, responseId);
            return ResponseEntity.ok("Delete successfully a response with id: " + responseId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Internal error: " + e.getMessage());
        }
    }

    @GetMapping("project/{projectId}/campaign/{campaignId}/survey/{surveyId}/response/all")
    @ApiMessage("Get all responses of a survey in a campaign")
    public ResponseEntity<?> getAllResponseOfSurvey(@PathVariable("projectId") long projectId,
            @PathVariable("campaignId") long campaignId,
            @PathVariable("surveyId") long surveyId) {
        try {
            List<Response> responses = this.responseService.getAllResponseOfSurvey(campaignId, surveyId);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Internal error: " + e.getMessage());
        }
    }
}
