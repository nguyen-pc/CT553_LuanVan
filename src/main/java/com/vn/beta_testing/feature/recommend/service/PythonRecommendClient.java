package com.vn.beta_testing.feature.recommend.service;

import com.vn.beta_testing.feature.recommend.dto.*;
import com.vn.beta_testing.feature.auth_service.DTO.UserProfileDTO;
import com.vn.beta_testing.feature.auth_service.service.ProfileService;
import com.vn.beta_testing.feature.company_service.DTO.UserRecruitProfileDTO;
import com.vn.beta_testing.feature.company_service.service.UserRecruitProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PythonRecommendClient {

    private final RestTemplate restTemplate;
    private final String pythonUrl;
    private final UserRecruitProfileService userRecruitProfileService;
    private final ProfileService profileService;

    public PythonRecommendClient(
            @Value("${python.recommend.url:http://localhost:8000}") String pythonUrl,
            UserRecruitProfileService userRecruitProfileService,
            ProfileService profileService) {
        this.restTemplate = new RestTemplate();
        this.pythonUrl = pythonUrl;
        this.userRecruitProfileService = userRecruitProfileService;
        this.profileService = profileService;
    }

    /**
     * Gọi FastAPI để lấy danh sách campaign phù hợp với userId
     */
    public List<RecommendResultDTO> getRecommendations(Long userId) {
        //  Lấy thông tin user
        UserProfileDTO userProfile = profileService.getProfileByUserId(userId);
        if (userProfile == null) {
            throw new RuntimeException("User profile not found for userId: " + userId);
        }

        UserAudienceDTO userAudience = mapUserProfileToDTO(userProfile);

        //  Lấy danh sách tất cả campaign profiles
        List<UserRecruitProfileDTO> campaignProfiles = userRecruitProfileService.getAll();

        List<CampaignProfileDTO> campaignDtos = campaignProfiles.stream().map(p -> {
            CampaignProfileDTO dto = new CampaignProfileDTO();
            dto.setId(p.getId());
            dto.setCampaignId(p.getCampaignId());
            dto.setGender(p.getGender() != null ? p.getGender().name() : null);
            dto.setCountry(p.getCountry());
            dto.setZipcode(p.getZipcode());
            dto.setHouseholdIncome(p.getHouseholdIncome());
            dto.setIsChildren(p.getIsChildren());
            dto.setEmployment(p.getEmployment());
            dto.setGamingGenres(p.getGamingGenres());
            dto.setBrowsers(p.getBrowsers());
            dto.setLanguages(p.getLanguages());
            dto.setOwnedDevices(p.getOwnedDevices());
            dto.setDevices(p.getDevices());
            return dto;
        }).collect(Collectors.toList());

        //  Chuẩn bị body request
        RecommendRequestDTO requestBody = new RecommendRequestDTO();
        requestBody.setUser(userAudience);
        requestBody.setCampaigns(campaignDtos);

        // Gửi POST sang FastAPI
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RecommendRequestDTO> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<RecommendResultDTO[]> response = restTemplate.exchange(
                    pythonUrl + "/recommend",
                    HttpMethod.POST,
                    entity,
                    RecommendResultDTO[].class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return List.of(response.getBody());
            } else {
                throw new RuntimeException("FastAPI returned invalid response");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error calling FastAPI /recommend: " + e.getMessage());
        }
    }

    // Map user profile -> DTO tương ứng FastAPI
    private UserAudienceDTO mapUserProfileToDTO(UserProfileDTO u) {
        UserAudienceDTO dto = new UserAudienceDTO();
        dto.setId(u.getId());
        dto.setCountry(u.getCountry());
        dto.setZipcode(u.getZipcode());
        dto.setGender(u.getGender() != null ? u.getGender().name() : null);
        dto.setLanguages(u.getLanguage());
        dto.setBrowsers(u.getBrowsers());
        dto.setComputer(u.getComputer());
        dto.setSmartPhone(null);
        dto.setOtherDevice(null);
        dto.setEmployment(u.getEmployment());
        dto.setGamingGenres(u.getGamingGenres());
        dto.setHouseholdIncome(u.getHouseholdIncome());
        dto.setChildren(u.getChildren());
        return dto;
    }
}
