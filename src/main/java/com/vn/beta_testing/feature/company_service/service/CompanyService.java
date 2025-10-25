package com.vn.beta_testing.feature.company_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.CompanyProfile;
import com.vn.beta_testing.feature.auth_service.repository.UserRepository;
import com.vn.beta_testing.feature.company_service.DTO.CompanyDTO;
import com.vn.beta_testing.feature.company_service.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    private CompanyDTO toDTO(CompanyProfile entity) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setCompanyEmail(entity.getCompanyEmail());
        dto.setCompanyAddress(entity.getCompanyAddress());
        dto.setCompanyPhoneNumber(entity.getCompanyPhoneNumber());
        dto.setCompanyWebsite(entity.getCompanyWebsite());
        dto.setCompanyMST(entity.getCompanyMST());
        dto.setCompanyAddressMST(entity.getCompanyAddressMST());
        dto.setDescription(entity.getDescription());
        dto.setLogo(entity.getLogo());
        dto.setBanner(entity.getBanner());
        dto.setActive(entity.isActive());
        return dto;
    }

    public CompanyProfile createCompanyProfile(CompanyProfile companyProfile) {
        return this.companyRepository.save(companyProfile);
    }

    public List<CompanyDTO> fetchAllCompaniesDTO() {
        return companyRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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

    public void handleDelete(long id) {
        this.companyRepository.deleteById(id);
    }

    public CompanyDTO fetchCompanyByUserId(Long userId) {
    CompanyProfile company = userRepository.findCompanyByUserId(userId);
    if (company == null) return null;
    return toDTO(company);
}

}
