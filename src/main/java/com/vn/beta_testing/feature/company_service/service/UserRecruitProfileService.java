package com.vn.beta_testing.feature.company_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.UserRecruitProfile;
import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.company_service.repository.UserRecruitProfileRepository;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
public class UserRecruitProfileService {

    private final UserRecruitProfileRepository userRecruitProfileRepository;
    private final CampaignService campaignService;

    public UserRecruitProfileService(UserRecruitProfileRepository userRecruitProfileRepository,
            CampaignService campaignService) {
        this.userRecruitProfileRepository = userRecruitProfileRepository;
        this.campaignService = campaignService;
    }

    public UserRecruitProfile fetchById(Long id) {
        return this.userRecruitProfileRepository.findById(id).orElse(null);
    }

    public UserRecruitProfile fetchByCampaignId(Long campaignId) {
        return this.userRecruitProfileRepository.findByCampaignId(campaignId);
    }

    public ResultPaginationDTO fetchAll(Specification<UserRecruitProfile> spec, Pageable pageable) {
        Page<UserRecruitProfile> page = this.userRecruitProfileRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(page.getContent());
        return rs;
    }

    public UserRecruitProfile create(UserRecruitProfile profile) {
        if(profile.getCampaign() == null){
            throw new IdInvalidException("Campaign must not be null.");
        }
        if (profile.getCampaign() != null) {
            Campaign campaign = this.campaignService.fetchCampaignById(profile.getCampaign().getId());
            System.out.println("Campaign: " + campaign);
            profile.setCampaign(campaign != null ? campaign : null);
        }
        return this.userRecruitProfileRepository.save(profile);
    }

    public UserRecruitProfile update(UserRecruitProfile profile) {
        UserRecruitProfile existing = this.userRecruitProfileRepository.findById(profile.getId()).orElse(null);
        if (existing == null) {
            throw new IdInvalidException("UserRecruitProfile with id = " + profile.getId() + " does not exist.");
        }

        existing.setRecruitMethod(profile.getRecruitMethod());
        existing.setTesterCount(profile.getTesterCount());
        existing.setDevices(profile.getDevices());
        existing.setWhitelist(profile.getWhitelist());
        existing.setGender(profile.getGender());
        existing.setCountry(profile.getCountry());
        existing.setZipcode(profile.getZipcode());
        existing.setHouseholdIncome(profile.getHouseholdIncome());
        existing.setIsChildren(profile.getIsChildren());
        existing.setEmployment(profile.getEmployment());
        existing.setGamingGenres(profile.getGamingGenres());
        existing.setBrowsers(profile.getBrowsers());
        existing.setSocialNetworks(profile.getSocialNetworks());
        existing.setWebExpertise(profile.getWebExpertise());
        existing.setLanguages(profile.getLanguages());
        existing.setOwnedDevices(profile.getOwnedDevices());

        if (profile.getCampaign() != null) {
            Campaign campaign = this.campaignService.fetchCampaignById(profile.getCampaign().getId());
            existing.setCampaign(campaign != null ? campaign : null);
        }

        return this.userRecruitProfileRepository.save(existing);
    }

    public void delete(Long id) {
        UserRecruitProfile existing = this.userRecruitProfileRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new IdInvalidException("UserRecruitProfile with id = " + id + " does not exist.");
        }
        this.userRecruitProfileRepository.delete(existing);
    }
}
