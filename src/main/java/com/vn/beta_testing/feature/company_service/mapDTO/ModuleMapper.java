package com.vn.beta_testing.feature.company_service.mapDTO;

import org.springframework.stereotype.Component;

import com.vn.beta_testing.domain.Module;
import com.vn.beta_testing.feature.company_service.DTO.ModuleRequestDTO;
import com.vn.beta_testing.feature.company_service.DTO.ModuleResponseDTO;

@Component
public class ModuleMapper {

    public ModuleResponseDTO toDTO(Module module) {
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

    public void updateEntity(Module module, ModuleRequestDTO req) {
        module.setModuleName(req.getModuleName());
        module.setDescription(req.getDescription());
        module.setRiskLevel(req.getRiskLevel());
    }
}
