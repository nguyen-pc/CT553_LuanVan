package com.vn.beta_testing.feature.bug_service.service;

import com.vn.beta_testing.domain.BugType;
import com.vn.beta_testing.feature.bug_service.repository.BugTypeRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BugTypeService {
    private final BugTypeRepository repository;

    public BugTypeService(BugTypeRepository repository) {
        this.repository = repository;
    }

    public BugType create(BugType bugType) {
        return repository.save(bugType);
    }

    public List<BugType> getAll() {
        return repository.findAll();
    }
}