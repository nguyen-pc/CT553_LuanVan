package com.vn.beta_testing.feature.company_service.controller;

import com.vn.beta_testing.domain.TesterInvite;
import com.vn.beta_testing.feature.company_service.service.TesterInviteService;
import com.vn.beta_testing.util.annotation.ApiMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recruit-profiles/invites")
@RequiredArgsConstructor
public class TesterInviteController {

    private final TesterInviteService testerInviteService;

    @PostMapping("/campaign/{campaignId}")
    @ApiMessage("Save tester invites for campaign")
    public ResponseEntity<List<TesterInvite>> saveInvites(
            @PathVariable Long campaignId,
            @RequestBody Map<String, List<String>> body) {

        List<String> emails = body.get("emailList");
        List<TesterInvite> saved = testerInviteService.saveEmailList(campaignId, emails);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/campaign/{campaignId}")
    @ApiMessage("Get all tester invites for campaign")
    public ResponseEntity<List<TesterInvite>> getInvites(@PathVariable Long campaignId) {
        List<TesterInvite> invites = testerInviteService.getInvitesByCampaign(campaignId);
        return ResponseEntity.ok(invites);
    }
}