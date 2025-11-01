package com.vn.beta_testing.feature.company_service.controller;

import java.util.List;

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

import com.vn.beta_testing.domain.CompanyProfile;
import com.vn.beta_testing.feature.company_service.DTO.CompanyDTO;
import com.vn.beta_testing.feature.company_service.service.CompanyService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/company/{id}")
    @ApiMessage("Get company by id")
    public ResponseEntity<CompanyProfile> getCompanyById(@PathVariable("id") Long id) {
        CompanyProfile companyProfile = this.companyService.fetchCompanyById(id);
        if (companyProfile == null) {
            throw new IdInvalidException("Company with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(companyProfile);
    }

    @PostMapping("/company/create")
    @ApiMessage("Create a new company")
    public ResponseEntity<CompanyProfile> createCompany(@RequestBody CompanyProfile companyProfile) {
        boolean isEmailExist = this.companyService.isEmailExist(companyProfile.getCompanyEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email" + companyProfile.getCompanyEmail() + "da ton tai, vui long su dung email khac");
        }

        CompanyProfile createdCompany = companyService.createCompanyProfile(companyProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/company/switch-status/{id}")
    @ApiMessage("Switch company status")
    public ResponseEntity<CompanyProfile> switchCompanyStatus(@PathVariable("id") Long id) {
        CompanyProfile companyProfile = this.companyService.fetchCompanyById(id);

        if (companyProfile == null) {
            throw new IdInvalidException("Company with id = " + id + " does not exist.");
        }
        companyProfile.setActive(!companyProfile.isActive());
        this.companyService.updateStatusCompanyProfile(companyProfile);
        return ResponseEntity.ok(companyProfile);
    }

    @PutMapping("/company/update/{id}")
    @ApiMessage("Update company by owner company")
    public ResponseEntity<CompanyProfile> updateCompany(@PathVariable("id") Long id,
            @RequestBody CompanyProfile upCompanyProfile) {
        CompanyProfile company = this.companyService.fetchCompanyById(id);

        if (company == null) {
            throw new IdInvalidException("Company with id = " + id + " does not exist.");
        }

        CompanyProfile cProfile = this.companyService.handleUpdateCompany(upCompanyProfile);

        return ResponseEntity.ok(cProfile);
    }

    @DeleteMapping("/company/delete/{id}")
    @ApiMessage("Delete company")
    public ResponseEntity<String> deleteCompany(@PathVariable("id") Long id) {
        CompanyProfile company = this.companyService.fetchCompanyById(id);

        if (company == null) {
            throw new IdInvalidException("Company with id = " + id + " does not exist.");
        }

        this.companyService.handleDelete(id);
        return ResponseEntity.status(HttpStatus.OK).body("delete success");
    }

    @GetMapping("/company")
    @ApiMessage("Get all companies")
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = this.companyService.fetchAllCompaniesDTO();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/company/user/{userId}")
    @ApiMessage("Get company by user ID")
    public ResponseEntity<CompanyDTO> getCompanyByUserId(@PathVariable("userId") Long userId) {
        CompanyDTO companyDTO = this.companyService.fetchCompanyByUserId(userId);

        if (companyDTO == null) {
            throw new IdInvalidException("User with id = " + userId + " does not have a company profile.");
        }

        return ResponseEntity.ok(companyDTO);
    }

    @GetMapping("/company/latest")
    @ApiMessage("Get top 10 newest companies")
    public ResponseEntity<List<CompanyDTO>> getTop10NewestCompanies() {
        List<CompanyDTO> latestCompanies = this.companyService.fetchTop10NewestCompanies();
        return ResponseEntity.ok(latestCompanies);
    }
}