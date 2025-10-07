package com.vn.beta_testing.feature.testcase_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.TestScenario;
import com.vn.beta_testing.domain.UseCase;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.testcase_service.repository.TestScenarioRepository;

@Service
public class TestScenarioService {
    private final TestScenarioRepository testScenarioRepository;
    private final UseCaseService useCaseService;

    public TestScenarioService(TestScenarioRepository testScenarioRepository, UseCaseService useCaseService) {
        this.testScenarioRepository = testScenarioRepository;
        this.useCaseService = useCaseService;
    }

     public ResultPaginationDTO fetchAll(Specification<TestScenario> spec, Pageable pageable) {
        Page<TestScenario> pageUser = this.testScenarioRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageUser.getContent());

        return rs;
    }

    public TestScenario createTestScenario(TestScenario testScenario) {
        if(testScenario.getUseCase() != null){
            UseCase useCase = this.useCaseService.fetchUseCaseById(testScenario.getUseCase().getId());
            testScenario.setUseCase(useCase != null ? useCase : null);
        }
        return testScenarioRepository.save(testScenario);
    }

    public TestScenario fetchTestScenarioById(Long id) {
        return this.testScenarioRepository.findById(id).orElse(null);
    }

    public TestScenario updateTestScenario(TestScenario testScenario) {
        TestScenario existingTestScenario = this.testScenarioRepository.findById(testScenario.getId()).orElse(null);
        if (existingTestScenario == null) {
            throw new IllegalArgumentException("TestScenario with id = " + testScenario.getId() + " does not exist.");
        }
        if(testScenario.getUseCase() != null){
            UseCase useCase = this.useCaseService.fetchUseCaseById(testScenario.getUseCase().getId());
            existingTestScenario.setUseCase(useCase != null ? useCase : null);
        }
        existingTestScenario.setTitle(testScenario.getTitle());
        existingTestScenario.setDescription(testScenario.getDescription());
        existingTestScenario.setPrecondition(testScenario.getPrecondition());
        return this.testScenarioRepository.save(existingTestScenario);
    }
    public void deleteTestScenario(Long id) {
        this.testScenarioRepository.deleteById(id);
    }
}
