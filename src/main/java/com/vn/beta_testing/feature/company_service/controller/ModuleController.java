package com.vn.beta_testing.feature.company_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.feature.company_service.DTO.ModuleRequestDTO;
import com.vn.beta_testing.feature.company_service.DTO.ModuleResponseDTO;
import com.vn.beta_testing.feature.company_service.service.ModuleService;
import com.vn.beta_testing.util.annotation.ApiMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    @ApiMessage("Module created successfully")
    public ResponseEntity<ModuleResponseDTO> createModule(@RequestBody ModuleRequestDTO req) {
        return ResponseEntity.ok(moduleService.createModule(req));
    }

    @PutMapping("/{id}")
    @ApiMessage("Module updated successfully")
    public ResponseEntity<ModuleResponseDTO> updateModule(
            @PathVariable("id") Long id,
            @RequestBody ModuleRequestDTO req) {
        return ResponseEntity.ok(moduleService.updateModule(id, req));
    }

    @GetMapping("/{id}")
    @ApiMessage("Module retrieved successfully")
    public ResponseEntity<ModuleResponseDTO> getModule(@PathVariable("id") Long id) {
        return ResponseEntity.ok(moduleService.getModuleById(id));
    }

    @GetMapping("/project/{projectId}")
    @ApiMessage("Modules retrieved successfully")
    public ResponseEntity<List<ModuleResponseDTO>> getModulesByProject(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(moduleService.getModulesByProject(projectId));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Module deleted successfully")
    public ResponseEntity<String> deleteModule(@PathVariable("id") Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
