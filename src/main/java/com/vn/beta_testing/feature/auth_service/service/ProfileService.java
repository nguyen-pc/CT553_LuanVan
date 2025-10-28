package com.vn.beta_testing.feature.auth_service.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.UserProfile;
import com.vn.beta_testing.feature.auth_service.DTO.UserProfileDTO;
import com.vn.beta_testing.feature.auth_service.mapper.ProfileMapper;
import com.vn.beta_testing.feature.auth_service.repository.ProfileRepository;
import com.vn.beta_testing.feature.auth_service.repository.UserRepository;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public UserProfileDTO getProfileByUserId(Long userId) {
        var profile = Optional.ofNullable(profileRepository.findByUserId(userId))
                .orElseThrow(() -> new RuntimeException("Profile not found for userId = " + userId));
        return ProfileMapper.toDTO(profile);
    }

    public UserProfileDTO saveOrUpdateProfile(Long userId, UserProfile profileData) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id = " + userId));

        UserProfile existing = profileRepository.findByUserId(userId);
        if (existing != null) {
            existing.setBirthYear(profileData.getBirthYear());
            existing.setAge(profileData.getAge());
            existing.setZipcode(profileData.getZipcode());
            existing.setCountry(profileData.getCountry());
            existing.setHouseholdIncome(profileData.getHouseholdIncome());
            existing.setChildren(profileData.isChildren());
            existing.setEmployment(profileData.getEmployment());
            existing.setEducation(profileData.getEducation());
            existing.setGamingGenres(profileData.getGamingGenres());
            existing.setBrowsers(profileData.getBrowsers());
            existing.setWebExpertise(profileData.getWebExpertise());
            existing.setLanguage(profileData.getLanguage());
            existing.setComputer(profileData.getComputer());
            existing.setSmartPhone(profileData.getSmartPhone());
            existing.setTablet(profileData.getTablet());
            existing.setOtherDevice(profileData.getOtherDevice());
            existing.setGender(profileData.getGender());
            return ProfileMapper.toDTO(profileRepository.save(existing));
        }

        profileData.setUser(user);
        return ProfileMapper.toDTO(profileRepository.save(profileData));
    }
}