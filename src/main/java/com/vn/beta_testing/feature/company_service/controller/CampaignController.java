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
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CampaignController {
    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/campaign/{id}")
    @ApiMessage("Get campaign by id")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable("id") Long id) {
        Campaign campaign = this.campaignService.fetchCampaignById(id);
        if (campaign == null) {
            throw new IdInvalidException("Campaign with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/campaign/create")
    @ApiMessage("Create a new campaign")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        Campaign createdCampaign = campaignService.createCampaign(campaign);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
    }

    @PutMapping("/campaign/update/{id}")
    @ApiMessage("Update campaign")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable("id") Long id, @RequestBody Campaign campaign) {
        Campaign existingCampaign = this.campaignService.fetchCampaignById(id);
        if (existingCampaign == null) {
            throw new IdInvalidException("Campaign with id = " + id + " does not exist.");
        }

        Campaign updatedCampaign = this.campaignService.updateCampaign(campaign);
        return ResponseEntity.ok(updatedCampaign);
    }
}
