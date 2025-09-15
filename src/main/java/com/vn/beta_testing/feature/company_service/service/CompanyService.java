package com.vn.beta_testing.feature.company_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.CompanyProfile;
import com.vn.beta_testing.feature.company_service.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyProfile createCompanyProfile(CompanyProfile companyProfile) {
        return this.companyRepository.save(companyProfile);
    }

    public boolean isEmailExist(String email) {
        return this.companyRepository.existsByCompanyEmail(email);
    }

    public CompanyProfile fetchCompanyById(Long companyId) {
        return this.companyRepository.findById(companyId).orElse(null);
    }

    public CompanyProfile updateCompanyProfile(CompanyProfile companyProfile) {
        return this.companyRepository.save(companyProfile);
    }
}
