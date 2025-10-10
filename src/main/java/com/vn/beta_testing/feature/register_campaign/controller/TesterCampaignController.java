package com.vn.beta_testing.feature.register_campaign.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.vn.beta_testing.domain.TesterCampaign;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterCampaignDTO;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterCampaignStatsDTO;
import com.vn.beta_testing.feature.register_campaign.service.TesterCampaignService;
import com.vn.beta_testing.util.annotation.ApiMessage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TesterCampaignController {

    private final TesterCampaignService testerCampaignService;

    public TesterCampaignController(TesterCampaignService testerCampaignService) {
        this.testerCampaignService = testerCampaignService;
    }

    @GetMapping("/tester-campaigns")
    @ApiMessage("Get all tester campaigns")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<TesterCampaign> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.testerCampaignService.fetchAll(spec, pageable));
    }

    @GetMapping("/campaign/{campaignId}/testers")
    @ApiMessage("Get testers by campaign")
    public ResponseEntity<List<TesterCampaignDTO>> getTestersByCampaign(@PathVariable("campaignId") Long campaignId) {
        List<TesterCampaign> testers = this.testerCampaignService.getTestersByCampaign(campaignId);
        List<TesterCampaignDTO> dtos = testers.stream()
                .map(this.testerCampaignService::toDTO)
                .toList();
        return ResponseEntity.ok().body(dtos);
    }

    @PostMapping("/campaign/tester-campaign/apply")
    @ApiMessage("Apply for campaign")
    public ResponseEntity<TesterCampaignDTO> apply(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Long campaignId = Long.valueOf(payload.get("campaignId").toString());
        String note = payload.get("note") != null ? payload.get("note").toString() : "";
        TesterCampaign created = this.testerCampaignService.applyForCampaign(userId, campaignId, note);

        TesterCampaignDTO response = this.testerCampaignService.toDTO(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/campaign/tester-campaign/{id}/approve")
    @ApiMessage("Approve tester")
    public ResponseEntity<TesterCampaignDTO> approve(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.testerCampaignService.toDTO(this.testerCampaignService.approveTester(id)));
    }

    @PutMapping("/campaign/tester-campaign/{id}/reject")
    @ApiMessage("Reject tester")
    public ResponseEntity<TesterCampaignDTO> reject(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.testerCampaignService.toDTO(this.testerCampaignService.rejectTester(id)));
    }

    @PutMapping("/campaign/tester-campaign/{id}/progress")
    @ApiMessage("Update tester progress")
    public ResponseEntity<TesterCampaignDTO> updateProgress(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Integer> body) {
        int progress = body.getOrDefault("progress", 0);
        return ResponseEntity
                .ok(this.testerCampaignService.toDTO(this.testerCampaignService.updateProgress(id, progress)));
    }

    @GetMapping("/campaign/{campaignId}/tester-campaign/stats")
    @ApiMessage("Get tester campaign statistics by campaign")
    public ResponseEntity<TesterCampaignStatsDTO> getStatsByCampaign(
            @PathVariable("campaignId") Long campaignId) {
        return ResponseEntity.ok(this.testerCampaignService.getStatsByCampaign(campaignId));
    }
}
