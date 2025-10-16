package com.vn.beta_testing.feature.survey_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.Question;
import com.vn.beta_testing.feature.survey_service.service.QuestionService;
import com.vn.beta_testing.feature.survey_service.service.SurveyService;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    private final QuestionService questionService;
    private final SurveyService surveyService;

    public QuestionController(QuestionService questionService, SurveyService surveyService) {
        this.questionService = questionService;
        this.surveyService = surveyService;
    }

    // Create the new question
    @PostMapping("/project/{projectId}/campaign/{campaignId}/survey/{surveyId}/question")
    public ResponseEntity<?> createQuestion(@PathVariable("projectId") long projectId,
            @PathVariable("campaignId") long campaignId,
            @PathVariable("surveyId") long surveyId,
            @RequestBody Question question) {
        try {
            Question createdQuestion = this.questionService.createQuestion(campaignId, surveyId, question);
            return ResponseEntity.ok(createdQuestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }

    // Update a question with idQuestion
    @PutMapping("/project/{projectId}/campaign/{campaignId}/survey/{surveyId}/question/{questionId}")
    public ResponseEntity<?> updateQuestion(@RequestBody Question resQuestion,
            @PathVariable("projectId") long projectId, @PathVariable("campaignId") long campaignId,
            @PathVariable("surveyId") long surveyId, @PathVariable("questionId") long questionId) {
        try {
            Question updatedQuestion = this.questionService.updateQuestion(campaignId, surveyId, questionId,
                    resQuestion);
            return ResponseEntity.ok(updatedQuestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }

    // Get a question with id
    @GetMapping("/project/{projectId}/campaign/{campaignId}/survey/{surveyId}/question/{questionId}")
    public ResponseEntity<?> getQuestion(@PathVariable("projectId") long projectId,
            @PathVariable("campaignId") long campaignId, @PathVariable("surveyId") long surveyId,
            @PathVariable("questionId") long questionId) {
        try {
            Question dbQuestion = this.questionService.getQuestion(campaignId, surveyId, questionId);
            return ResponseEntity.ok(dbQuestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Internal error: " + e.getMessage());
        }
    }

    // Delete a question with id
    @DeleteMapping("/project/{projectId}/campaign/{campaignId}/survey/{surveyId}/question/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("projectId") long projectId,
            @PathVariable("campaignId") long campaignId, @PathVariable("surveyId") long surveyId,
            @PathVariable("questionId") long questionId) {
        try {
            this.questionService.deleteQuestion(campaignId, surveyId, questionId);
            return ResponseEntity.ok("Deleted successfully question with id: " + questionId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal error: " + e.getMessage());
        }
    }

    @GetMapping("/campaign/{campaignId}/survey/{surveyId}/question/all")
    public ResponseEntity<?> getAllQuestionOfSurvey(@PathVariable("campaignId") long campaignId,
            @PathVariable("surveyId") long surveyId) {
        try {
            List<Question> questions = this.questionService.getAllQuestionOfSurvey(campaignId, surveyId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Internal error: " + e.getMessage());
        }
    }
}
