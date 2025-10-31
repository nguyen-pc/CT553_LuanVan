package com.vn.beta_testing.feature.company_service.mapDTO;

import com.vn.beta_testing.domain.UserRecruitProfile;
import com.vn.beta_testing.feature.company_service.DTO.UserRecruitProfileDTO;

import org.springframework.stereotype.Component;

@Component
public class UserRecruitProfileMapper {

    public UserRecruitProfileDTO toDTO(UserRecruitProfile e) {
        if (e == null) return null;

        UserRecruitProfileDTO dto = new UserRecruitProfileDTO();
        dto.setId(e.getId());
        dto.setRecruitMethod(e.getRecruitMethod());
        dto.setTesterCount(e.getTesterCount());
        dto.setDevices(e.getDevices());
        dto.setWhitelist(e.getWhitelist());

        dto.setGender(e.getGender());
        dto.setCountry(e.getCountry());
        dto.setZipcode(e.getZipcode());
        dto.setHouseholdIncome(e.getHouseholdIncome());
        dto.setIsChildren(e.getIsChildren());
        dto.setEmployment(e.getEmployment());
        dto.setGamingGenres(e.getGamingGenres());
        dto.setBrowsers(e.getBrowsers());
        dto.setSocialNetworks(e.getSocialNetworks());
        dto.setWebExpertise(e.getWebExpertise());
        dto.setLanguages(e.getLanguages());
        dto.setOwnedDevices(e.getOwnedDevices());

        // tránh lazy proxy
        if (e.getCampaign() != null) {
            dto.setCampaignId(e.getCampaign().getId());
        }

        return dto;
    }
}