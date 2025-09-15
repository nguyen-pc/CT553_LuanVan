package com.vn.beta_testing.feature.company_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.CompanyProfile;
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

    @PutMapping("/company/switchStatus/{id}")
    @ApiMessage("Switch company status")
    public ResponseEntity<CompanyProfile> switchCompanyStatus(@PathVariable("id") Long id) {
        CompanyProfile companyProfile = this.companyService.fetchCompanyById(id);

        if(companyProfile == null) {
            throw new IdInvalidException("Company with id = " + id + " does not exist.");
        }
        companyProfile.setActive(!companyProfile.isActive());
        this.companyService.updateCompanyProfile(companyProfile);
        return ResponseEntity.ok(companyProfile);
    }

    
}