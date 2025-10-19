package com.vn.beta_testing.feature.bug_service.controller;

import com.vn.beta_testing.domain.BugType;
import com.vn.beta_testing.feature.bug_service.service.BugTypeService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bugs/bug-type")
public class BugTypeController {

    private final BugTypeService service;

    public BugTypeController(BugTypeService service) {
        this.service = service;
    }

    @PostMapping
    public BugType create(@RequestBody BugType bugType) {
        return service.create(bugType);
    }

    @GetMapping
    public List<BugType> getAll() {
        return service.getAll();
    }
}