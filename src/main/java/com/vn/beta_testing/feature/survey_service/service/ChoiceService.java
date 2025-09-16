package com.vn.beta_testing.feature.survey_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.Choice;
import com.vn.beta_testing.feature.survey_service.repository.ChoiceRepository;

@Service
public class ChoiceService {
    private final ChoiceRepository choiceRepository;
      public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    public Choice getChoice(Long choiceId) {
        Choice dbChoice = this.choiceRepository.findById(choiceId).orElse(null);
        if (dbChoice == null)
            throw new IllegalArgumentException("Could not find choice with id: " + choiceId);
        return dbChoice;
    }
}
