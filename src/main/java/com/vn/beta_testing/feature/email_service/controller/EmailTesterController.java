package com.vn.beta_testing.feature.email_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.feature.email_service.DTO.EmailTesterDTO;
import com.vn.beta_testing.feature.email_service.service.EmailTesterService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.constant.EmailStatus;

@RestController
@RequestMapping("/api/v1/email-testers")
public class EmailTesterController {

    private final EmailTesterService emailTesterService;

    public EmailTesterController(EmailTesterService emailTesterService) {
        this.emailTesterService = emailTesterService;
    }

    @GetMapping("/campaign/{campaignId}")
    @ApiMessage("Get all email testers by campaign ID")
    public ResponseEntity<List<EmailTesterDTO>> getByCampaign(@PathVariable("campaignId") Long campaignId) {
        return ResponseEntity.ok(emailTesterService.getAllByCampaign(campaignId));
    }

    @PostMapping("/campaign/{campaignId}")
    @ApiMessage("Add a new email tester to campaign")
    public ResponseEntity<EmailTesterDTO> create(@PathVariable("campaignId") Long campaignId,
            @RequestBody EmailTesterDTO dto) {
        return ResponseEntity.ok(emailTesterService.create(campaignId, dto));
    }

    @PutMapping("/{id}/status")
    @ApiMessage("Update email tester status")
    public ResponseEntity<EmailTesterDTO> updateStatus(@PathVariable("id") Long id,
            @RequestParam("status") EmailStatus status) {
        return ResponseEntity.ok(emailTesterService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete email tester")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        emailTesterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/campaign/{campaignId}/send")
    @ApiMessage("Send invitation emails to all testers of a campaign")
    public ResponseEntity<List<EmailTesterDTO>> sendCampaignInvitations(
            @PathVariable("campaignId") Long campaignId,
            @RequestBody(required = false) String customMessage // optional
    ) {
        return ResponseEntity.ok(emailTesterService.sendInvitations(campaignId, customMessage));
    }
}