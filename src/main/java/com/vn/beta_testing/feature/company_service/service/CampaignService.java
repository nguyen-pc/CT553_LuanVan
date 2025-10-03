package com.vn.beta_testing.feature.company_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.CampaignType;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.company_service.repository.CampaignRepository;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final ProjectService projectService;
    private final CampaignTypeService campaignTypeService;

    public CampaignService(CampaignRepository campaignRepository, ProjectService projectService, CampaignTypeService campaignTypeService) {
        this.campaignRepository = campaignRepository;
        this.projectService = projectService;
        this.campaignTypeService = campaignTypeService;
    }

    public Campaign fetchCampaignById(Long id) {
        return this.campaignRepository.findById(id).orElse(null);
    }

     public ResultPaginationDTO fetchAll(Specification<Campaign> spec, Pageable pageable) {
        Page<Campaign> pageUser = this.campaignRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageUser.getContent());

        return rs;
    }


    public Campaign createCampaign(Campaign campaign) {
        if(campaign.getProject() !=null){
            Project project = this.projectService.fetchProjectById(campaign.getProject().getId());
            campaign.setProject(project != null ? project : null);
        }
        if(campaign.getCampaignType() != null){
            // Assuming you have a CampaignTypeService to fetch CampaignType by ID
            CampaignType campaignType = this.campaignTypeService.fetchCampaignTypeById(campaign.getCampaignType().getId());
            campaign.setCampaignType(campaignType != null ? campaignType : null);
        }
        return this.campaignRepository.save(campaign);
    }

    public Campaign updateCampaign(Campaign campaign) {
        Campaign existingCampaign = this.campaignRepository.findById(campaign.getId()).orElse(null);
        if (existingCampaign == null) {
            throw new IdInvalidException("Campaign with id = " + campaign.getId() + " does not exist.");
        }
        existingCampaign.setTitle(campaign.getTitle());
        existingCampaign.setDescription(campaign.getDescription());
        existingCampaign.setInstructions(campaign.getInstructions());
        existingCampaign.setRewardValue(campaign.getRewardValue());
        existingCampaign.setRewardType(campaign.getRewardType());
        existingCampaign.setStatus(campaign.getStatus());
        existingCampaign.setStartDate(campaign.getStartDate());
        existingCampaign.setEndDate(campaign.getEndDate());
        if(campaign.getProject() !=null){
            Project project = this.projectService.fetchProjectById(campaign.getProject().getId());
            existingCampaign.setProject( project != null ? project : null);
        }
        if(campaign.getCampaignType() != null){
            // Assuming you have a CampaignTypeService to fetch CampaignType by ID
            CampaignType campaignType = this.campaignTypeService.fetchCampaignTypeById(campaign.getCampaignType().getId());
            existingCampaign.setCampaignType(campaignType != null ? campaignType : null);
        }
        return this.campaignRepository.save(existingCampaign);
    }

}
