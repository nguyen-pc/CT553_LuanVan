package com.vn.beta_testing.feature.company_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.CampaignType;
import com.vn.beta_testing.feature.company_service.repository.CampaignTypeRepository;

@Service
public class CampaignTypeService {
    private final CampaignTypeRepository campaignTypeRepository;

    public CampaignTypeService(CampaignTypeRepository campaignTypeRepository) {
        this.campaignTypeRepository = campaignTypeRepository;
    }

    public CampaignType createCampaignType(CampaignType campaignType) {
        return campaignTypeRepository.save(campaignType);
    }

    public CampaignType fetchCampaignTypeById(Long id) {
        return this.campaignTypeRepository.findById(id).orElse(null);
    }

    public CampaignType updateCampaignType(CampaignType campaignType) {
        CampaignType existingCampaignType = this.campaignTypeRepository.findById(campaignType.getId()).orElse(null);
        if (existingCampaignType == null) {
            throw new IllegalArgumentException("CampaignType with id = " + campaignType.getId() + " does not exist.");
        }
        existingCampaignType.setName(campaignType.getName());
        existingCampaignType.setDescription(campaignType.getDescription());
        return this.campaignTypeRepository.save(existingCampaignType);
    }
    public void deleteCampaignType(Long id) {
        this.campaignTypeRepository.deleteById(id);
    }
}
