package com.vn.beta_testing.feature.testcase_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.UseCase;
import com.vn.beta_testing.feature.testcase_service.repository.UseCaseRepository;

@Service
public class UseCaseService {
    private final UseCaseRepository useCaseRepository;
    public UseCaseService(UseCaseRepository useCaseRepository) {
        this.useCaseRepository = useCaseRepository;
    }

    public UseCase createUseCase(UseCase useCase) {
        return useCaseRepository.save(useCase);
    }

    public UseCase fetchUseCaseById(Long id) {
        return useCaseRepository.findById(id).orElse(null);
    }

    public UseCase updateUseCase(UseCase useCase) {
        return useCaseRepository.save(useCase);
    }
    public void deleteUseCase(Long id) {
        this.useCaseRepository.deleteById(id);
    }
}
