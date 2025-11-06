package com.vn.beta_testing.feature.company_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.ProjectUser;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.auth_service.repository.UserRepository;
import com.vn.beta_testing.feature.company_service.DTO.ProjectUserDTO;
import com.vn.beta_testing.feature.company_service.repository.ProjectRepository;
import com.vn.beta_testing.feature.company_service.repository.ProjectUserRepository;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
@Transactional
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public ProjectUserService(ProjectUserRepository projectUserRepo,
            ProjectRepository projectRepo,
            UserRepository userRepo) {
        this.projectUserRepo = projectUserRepo;
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    public ProjectUserDTO addUserToProject(Long projectId, Long userId, String role) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new IdInvalidException("Project not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IdInvalidException("User not found"));

        projectUserRepo.findByProjectIdAndUserId(projectId, userId)
                .ifPresent(pu -> {
                    throw new IdInvalidException("User already exists in this project");
                });

        ProjectUser saved = projectUserRepo.save(ProjectUser.builder()
                .project(project)
                .user(user)
                .role(role)
                .joinedAt(LocalDateTime.now())
                .build());

        return ProjectUserDTO.builder()
                .id(saved.getId())
                .projectId(project.getId())
                .projectTitle(project.getProjectName())
                .userId(user.getId())
                .userName(user.getName())
                .role(saved.getRole())
                .joinedAt(saved.getJoinedAt())
                .build();
    }

    public List<ProjectUserDTO> getUsersByProject(Long projectId) {
        return projectUserRepo.findByProjectId(projectId).stream()
                .map(pu -> ProjectUserDTO.builder()
                        .id(pu.getId())
                        .projectId(pu.getProject().getId())
                        .projectTitle(pu.getProject().getProjectName()) 
                        .userId(pu.getUser().getId())
                        .userName(pu.getUser().getName())
                        .role(pu.getRole())
                        .joinedAt(pu.getJoinedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void removeUserFromProject(Long projectId, Long userId) {
        ProjectUser pu = projectUserRepo.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new IdInvalidException("User not found in project"));
        projectUserRepo.delete(pu);
    }
}