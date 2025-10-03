package com.vn.beta_testing.feature.company_service.service;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.CompanyProfile;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.company_service.repository.ProjectRepository;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final CompanyService companyProfileService;

    public ProjectService(ProjectRepository projectRepository, CompanyService companyProfileService) {
        this.projectRepository = projectRepository;
        this.companyProfileService = companyProfileService; 
    }

    public Project fetchProjectById(Long id) {
        return this.projectRepository.findById(id).orElse(null);
    }

     public ResultPaginationDTO fetchAll(Specification<Project> spec, Pageable pageable) {
        Page<Project> pageUser = this.projectRepository.findAll(spec, pageable);

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


    public Project createProject(Project project) {
        
        if(project.getCompanyProfile() != null) {
            CompanyProfile company = this.companyProfileService.fetchCompanyById(project.getCompanyProfile().getId());
            project.setCompanyProfile(company != null ? company : null);
        }
        return this.projectRepository.save(project);
    }

    public Project updateProject(Project project) {
        Project existingProject = this.projectRepository.findById(project.getId()).orElse(null);
        if (existingProject == null) {
            throw new IdInvalidException("Project with id = " + project.getId() + " does not exist.");
        }
        existingProject.setProjectName(project.getProjectName());
        existingProject.setDescription(project.getDescription());
        existingProject.setStatus(project.isStatus());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());
        if(project.getCompanyProfile() != null) {
            CompanyProfile company = this.companyProfileService.fetchCompanyById(project.getCompanyProfile().getId());
            existingProject.setCompanyProfile(company != null ? company : null);
        }
        return this.projectRepository.save(existingProject);
    }

    public void deleteProject(Long id) {
        this.projectRepository.deleteById(id);
    }
}
