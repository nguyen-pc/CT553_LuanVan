package com.vn.beta_testing.feature.company_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.vn.beta_testing.feature.company_service.DTO.ProjectUserDTO;
import com.vn.beta_testing.feature.company_service.service.ProjectUserService;
import com.vn.beta_testing.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/project-users")
public class ProjectUserController {

    private final ProjectUserService projectUserService;

    public ProjectUserController(ProjectUserService projectUserService) {
        this.projectUserService = projectUserService;
    }

    @PostMapping("/add")
    @ApiMessage("Add user to project")
    public ResponseEntity<ProjectUserDTO> addUserToProject(
            @RequestParam("projectId") Long projectId,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "role", defaultValue = "Member") String role) {
        return ResponseEntity.ok(projectUserService.addUserToProject(projectId, userId, role));
    }

    @GetMapping("/{projectId}/users")
    @ApiMessage("Get all users in project")
    public ResponseEntity<List<ProjectUserDTO>> getUsersByProject(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectUserService.getUsersByProject(projectId));
    }

    @DeleteMapping("/remove")
    @ApiMessage("Remove user from project")
    public ResponseEntity<Void> removeUserFromProject(
            @RequestParam("projectId") Long projectId,
            @RequestParam("userId") Long userId) {
        projectUserService.removeUserFromProject(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}