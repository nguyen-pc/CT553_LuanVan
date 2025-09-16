package com.vn.beta_testing.feature.survey_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.Choice;
import com.vn.beta_testing.domain.Question;
import com.vn.beta_testing.domain.Survey;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.survey_service.repository.QuestionRepository;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final SurveyService surveyService;
    private final CampaignService campaignService;

    public QuestionService(QuestionRepository questionRepository, SurveyService surveyService,
            CampaignService campaignService) {
        this.questionRepository = questionRepository;
        this.surveyService = surveyService;
        this.campaignService = campaignService;
    }

     public void checkParameter(Long campaignId, Long surveyId) {
        // Check if the Campaign exists
        Campaign dbCampaign = this.campaignService.fetchCampaignById(campaignId);
        if (dbCampaign == null)
            throw new IllegalArgumentException("Could not find campaign with id: " + campaignId + " in DB");
        // Check if the Survey exists
        Survey dbSurvey = this.surveyService.getSurveyById(surveyId);
        if (dbSurvey == null)
            throw new IllegalArgumentException("Could not find survey with id: " + surveyId + " in DB");
        // Check if the Campaign contains the Survey
        if (campaignId != dbSurvey.getCampaign().getId())
            throw new IllegalArgumentException("Campaign #" + campaignId + " DOES NOT have survey #" + surveyId);
    }

    public Question createQuestion(Long campaignId, Long surveyId, Question question) {
        checkParameter(campaignId, surveyId);
        Survey dbSurvey = this.surveyService.getSurveyById(surveyId);
        // check if MULTIPLE_CHOICE or CHECKBOX have not any choice
        if ((question.getQuestionType().isMultipleOrCheckbox()) && question.getChoices() == null)
            throw new IllegalArgumentException("Multiple choice/ Checkbox question could not have empty choices");

        question.setSurvey(dbSurvey);

        // create question without choices
        List<Choice> choices = question.getChoices();
        question.setChoices(null);
        Question initialQuestion = this.questionRepository.save(question);
        if (initialQuestion == null)
            throw new RuntimeException("init question is WRONG");

        // only create choice if multiple_choice or checkbox question
        if (question.getQuestionType().isMultipleOrCheckbox()) {
            // set question for each choice in choices
            for (Choice choice : choices)
                choice.setQuestion(initialQuestion);

            // create question with choices
            initialQuestion.setChoices(choices);
            this.questionRepository.save(initialQuestion);
            if (initialQuestion == null) {
                this.questionRepository.deleteById(initialQuestion.getQuestionId());
                throw new RuntimeException("create question is WRONG");
            }
        }
        return initialQuestion;
    }

    public Question getQuestion(Long campaignId, Long surveyId, Long questionId) {
        checkParameter(campaignId, surveyId);
        Question dbQuestion = this.questionRepository.findById(questionId).orElse(null);
        if (dbQuestion == null)
            throw new IllegalArgumentException("Could not find question with id: " + questionId);
        if (surveyId != dbQuestion.getSurvey().getSurveyId())
            throw new IllegalArgumentException("Survey: " + surveyId + " DOES NOT CONTAIN question: " + questionId);
        return dbQuestion;
    }

    public void deleteQuestion(Long campaignId, Long surveyId, Long questionId) {
        checkParameter(campaignId, surveyId);
        Question dbQuestion = this.questionRepository.findById(questionId).orElse(null);
        if (dbQuestion == null)
            throw new IllegalArgumentException("Could not find question with id: " + questionId);
        if (surveyId != dbQuestion.getSurvey().getSurveyId())
            throw new IllegalArgumentException("Survey: " + surveyId + " DOES NOT CONTAIN question: " + questionId);
        this.questionRepository.deleteById(questionId);
    }

    public Question updateQuestion(Long campaignId, Long surveyId, Long questionId, Question resQuestion) {
        Question dbQuestion = this.getQuestion(campaignId, surveyId, questionId);
        if (dbQuestion == null) {
            throw new IllegalArgumentException("Could not find question with id: " + questionId);
        }

        dbQuestion.setQuestionName(resQuestion.getQuestionName());
        dbQuestion.setQuestionType(resQuestion.getQuestionType());

        if (resQuestion.getQuestionType().isMultipleOrCheckbox()) {
            if (resQuestion.getChoices() == null || resQuestion.getChoices().isEmpty())
                throw new IllegalArgumentException(
                        "resQuestion is MULTIPLE_CHOICE or CHECKBOX but does not have any choice!");

            // remove the association between Choice and Question
            dbQuestion.getChoices().forEach(choice -> choice.setQuestion(null));
            // remove all Choice from Question's list to ensure Hibernate detects the change
            dbQuestion.getChoices().clear();
            this.questionRepository.save(dbQuestion);
            // update choices
            for (Choice choice : resQuestion.getChoices())
                choice.setQuestion(dbQuestion);
            dbQuestion.getChoices().addAll(resQuestion.getChoices());
        } else {
            // remove the association between Choice and Question
            dbQuestion.getChoices().forEach(choice -> choice.setQuestion(null));
            // remove all Choice from Question's list to ensure Hibernate detects the change
            dbQuestion.getChoices().clear();
        }
        return this.questionRepository.save(dbQuestion);
    }

    public List<Question> getAllQuestionOfSurvey(Long campaignId, Long surveyId) {
        checkParameter(campaignId, surveyId);
        return this.questionRepository.findBySurvey_SurveyId(surveyId);
    }
}
