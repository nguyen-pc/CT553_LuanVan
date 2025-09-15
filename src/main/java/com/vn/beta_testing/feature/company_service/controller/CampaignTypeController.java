package com.vn.beta_testing.feature.company_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.CampaignType;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.company_service.service.CampaignTypeService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CampaignTypeController {
    private final CampaignTypeService campaignTypeService;  
    public CampaignTypeController(CampaignTypeService campaignTypeService) {
        this.campaignTypeService = campaignTypeService;
    }
    @GetMapping("/campaign-type/{id}")
    @ApiMessage("Get campaign type by id")
    public ResponseEntity<CampaignType> getCampaignTypeById(@PathVariable("id") Long id) {
        CampaignType campaignType = campaignTypeService.fetchCampaignTypeById(id);
        if (campaignType == null) {
            throw new IdInvalidException("Campaign type with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(campaignType);
    }

    @PostMapping("/campaign-type/create")
    @ApiMessage("Create a new campaign type")
    public ResponseEntity<CampaignType> createCampaignType(@RequestBody CampaignType campaignType) {
        CampaignType createdCampaignType = campaignTypeService.createCampaignType(campaignType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaignType);
    }

    @PutMapping("/campaign-type/update/{id}")
    @ApiMessage("Update campaign type")
    public ResponseEntity<CampaignType> updateCampaignType(@PathVariable("id") Long id, @RequestBody CampaignType campaignType) {
        CampaignType existingCampaignType = this.campaignTypeService.fetchCampaignTypeById(id);
        if (existingCampaignType == null) {
            throw new IdInvalidException("Campaign type with id = " + id + " does not exist.");
        }
        CampaignType updatedCampaignType = this.campaignTypeService.updateCampaignType(campaignType);
        return ResponseEntity.ok(updatedCampaignType);
    }

    @DeleteMapping("/campaign-type/delete/{id}")
    @ApiMessage("Delete campaign type")
    public ResponseEntity<Void> deleteCampaignType(@PathVariable("id") Long id) {
        CampaignType existingCampaignType = this.campaignTypeService.fetchCampaignTypeById(id);
        if (existingCampaignType == null) {
            throw new IdInvalidException("Campaign type with id = " + id + " does not exist.");
        }
        this.campaignTypeService.deleteCampaignType(id);
        return ResponseEntity.noContent().build();
    }

}
