package com.vn.beta_testing.feature.company_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.Module;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.feature.company_service.DTO.ModuleRequestDTO;
import com.vn.beta_testing.feature.company_service.DTO.ModuleResponseDTO;
import com.vn.beta_testing.feature.company_service.repository.ModuleRepository;
import com.vn.beta_testing.feature.company_service.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ProjectRepository projectRepository;

    private ModuleResponseDTO toDTO(Module module) {
        ModuleResponseDTO dto = new ModuleResponseDTO();
        dto.setId(module.getId());
        dto.setModuleName(module.getModuleName());
        dto.setDescription(module.getDescription());
        dto.setRiskLevel(module.getRiskLevel());

        if (module.getProject() != null) {
            dto.setProjectId(module.getProject().getId());
            dto.setProjectName(module.getProject().getProjectName());
        }

        dto.setCreatedBy(module.getCreatedBy());
        dto.setUpdatedBy(module.getUpdatedBy());

        return dto;
    }

    private void updateEntityFromDTO(Module module, ModuleRequestDTO req) {
        module.setModuleName(req.getModuleName());
        module.setDescription(req.getDescription());
        module.setRiskLevel(req.getRiskLevel());
    }

    public ModuleResponseDTO createModule(ModuleRequestDTO req) {

        Project project = projectRepository.findById(req.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Module module = new Module();
        updateEntityFromDTO(module, req);
        module.setProject(project);

        return toDTO(moduleRepository.save(module));
    }

    public ModuleResponseDTO updateModule(Long id, ModuleRequestDTO req) {

        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        updateEntityFromDTO(module, req);

        return toDTO(moduleRepository.save(module));
    }

    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new RuntimeException("Module not found");
        }
        moduleRepository.deleteById(id);
    }

    public Module fetchModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }

    public ModuleResponseDTO getModuleById(Long id) {
        return moduleRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }

    public ModuleResponseDTO getModuleByCampaignId(Long campaignId) {
        return moduleRepository.findModuleByCampaignId(campaignId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }

    public List<ModuleResponseDTO> getModulesByProject(Long projectId) {
        return moduleRepository.findByProjectId(projectId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

}
