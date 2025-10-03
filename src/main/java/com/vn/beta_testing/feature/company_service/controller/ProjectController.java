package com.vn.beta_testing.feature.company_service.controller;


import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import com.turkraft.springfilter.boot.Filter;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.company_service.service.ProjectService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    @ApiMessage("Get project with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllProject(
            @Filter Specification<Project> spec,
            Pageable pageable) {

        return ResponseEntity.ok().body(this.projectService.fetchAll(spec, pageable));
    }

    @GetMapping("/projects/all/{companyId}")
    @ApiMessage("Get project with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllProjectByCompany(
            @PathVariable("companyId") long companyId,
            @Filter Specification<Project> spec,
            Pageable pageable) {
        // Tạo specification lọc theo companyId
        Specification<Project> companySpec = (root, query, builder) -> builder.equal(root.get("companyProfile").get("id"),
                companyId);

        Specification<Project> finalSpec = Specification.where(companySpec).and(spec);
        return ResponseEntity.ok().body(this.projectService.fetchAll(finalSpec, pageable));
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") Long id) {
        Project project = projectService.fetchProjectById(id);
        if (project == null) {
            throw new IdInvalidException("Project with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(project);
    }

    @PostMapping("/projects/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/projects/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody Project project) {
        project.setId(id);
        Project updatedProject = projectService.updateProject(project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        System.out.println("Deleting project with id: " + id);
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
