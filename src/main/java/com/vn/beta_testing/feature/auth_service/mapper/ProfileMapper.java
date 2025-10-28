package com.vn.beta_testing.feature.auth_service.mapper;

import com.vn.beta_testing.domain.UserProfile;
import com.vn.beta_testing.feature.auth_service.DTO.UserProfileDTO;

public class ProfileMapper {

    public static UserProfileDTO toDTO(UserProfile entity) {
        if (entity == null) return null;

        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(entity.getId());
        dto.setBirthYear(entity.getBirthYear());
        dto.setAge(entity.getAge());
        dto.setZipcode(entity.getZipcode());
        dto.setCountry(entity.getCountry());
        dto.setHouseholdIncome(entity.getHouseholdIncome());
        dto.setChildren(entity.isChildren());
        dto.setEmployment(entity.getEmployment());
        dto.setEducation(entity.getEducation());
        dto.setGamingGenres(entity.getGamingGenres());
        dto.setBrowsers(entity.getBrowsers());
        dto.setWebExpertise(entity.getWebExpertise());
        dto.setLanguage(entity.getLanguage());
        dto.setComputer(entity.getComputer());
        dto.setSmartPhone(entity.getSmartPhone());
        dto.setTablet(entity.getTablet());
        dto.setOtherDevice(entity.getOtherDevice());
        dto.setGender(entity.getGender());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUsername(entity.getUser().getName());
        }

        return dto;
    }
}
