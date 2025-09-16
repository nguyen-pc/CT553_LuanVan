package com.vn.beta_testing.feature.testcase_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.UseCase;
import com.vn.beta_testing.feature.testcase_service.service.UseCaseService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UseCaseController {
    private final UseCaseService useCaseService;  
    public UseCaseController(UseCaseService useCaseService) {
        this.useCaseService = useCaseService;
    }

    @GetMapping("/usecase/{id}")
    @ApiMessage("Get use case by id")
    public ResponseEntity<UseCase> getUseCaseById(@PathVariable("id") Long id) {
        UseCase useCase = useCaseService.fetchUseCaseById(id);
        if (useCase == null) {
            throw new IdInvalidException("Use case with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(useCase);
    }

    @PostMapping("/usecase/create")
    @ApiMessage("Create a new use case")
    public ResponseEntity<UseCase> createUseCase(@RequestBody UseCase useCase) {
        UseCase createdUseCase = useCaseService.createUseCase(useCase);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUseCase);
    }

    @PutMapping("/usecase/update/{id}")
    @ApiMessage("Update use case")
    public ResponseEntity<UseCase> updateUseCase(@PathVariable("id") Long id, @RequestBody UseCase useCase) {
        UseCase existingUseCase = this.useCaseService.fetchUseCaseById(id);
        if (existingUseCase == null) {
            throw new IdInvalidException("Use case with id = " + id + " does not exist.");
        }
        UseCase updatedUseCase = this.useCaseService.updateUseCase(useCase);
        return ResponseEntity.ok(updatedUseCase);
    }
    @DeleteMapping("/usecase/delete/{id}")
    @ApiMessage("Delete use case")
    public ResponseEntity<Void> deleteUseCase(@PathVariable("id") Long id) {
        UseCase existingUseCase = this.useCaseService.fetchUseCaseById(id);
        if (existingUseCase == null) {
            throw new IdInvalidException("Use case with id = " + id + " does not exist.");
        }
        // Assuming a delete method exists in UseCaseService
        this.useCaseService.deleteUseCase(id);
        return ResponseEntity.noContent().build();
    }
    
}
