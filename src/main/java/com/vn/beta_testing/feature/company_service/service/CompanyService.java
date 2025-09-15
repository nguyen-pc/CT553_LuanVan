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

    public CompanyProfile updateStatusCompanyProfile(CompanyProfile companyProfile) {
        return this.companyRepository.save(companyProfile);
    }

    public CompanyProfile handleUpdateCompany(CompanyProfile companyProfile) {
        CompanyProfile getCompanyProfile = fetchCompanyById(companyProfile.getId());
        if (getCompanyProfile != null) {
            getCompanyProfile.setCompanyName(companyProfile.getCompanyName());
            getCompanyProfile.setCompanyPhoneNumber(companyProfile.getCompanyPhoneNumber());
            getCompanyProfile.setBanner(companyProfile.getBanner());
            getCompanyProfile.setCompanyAddress(companyProfile.getCompanyAddress());
            getCompanyProfile.setCompanyEmail(companyProfile.getCompanyEmail());
            getCompanyProfile.setCompanyWebsite(companyProfile.getCompanyWebsite());
            getCompanyProfile.setCompanyAddressMST(companyProfile.getCompanyAddressMST());
            getCompanyProfile.setCompanyDateMST(companyProfile.getCompanyDateMST());

        }
        return this.companyRepository.save(getCompanyProfile);
    }

    public void handleDelete(long id){
        this.companyRepository.deleteById(id);
    }
}
