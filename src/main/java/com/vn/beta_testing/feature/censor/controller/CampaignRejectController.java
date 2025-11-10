package com.vn.beta_testing.feature.censor.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.feature.censor.DTO.CampaignRejectMessageDTO;
import com.vn.beta_testing.feature.censor.DTO.CampaignRejectReasonDTO;
import com.vn.beta_testing.feature.censor.service.CampaignRejectService;
import com.vn.beta_testing.util.annotation.ApiMessage;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/campaigns/reject")
@RequiredArgsConstructor
public class CampaignRejectController {

    private final CampaignRejectService rejectService;

    @PostMapping("/{campaignId}")
    @ApiMessage("Tạo lý do từ chối cho campaign")
    public ResponseEntity<CampaignRejectReasonDTO> createRejectReason(
            @PathVariable("campaignId") Long campaignId,
            @RequestBody String reasonText) {
        return ResponseEntity.ok(rejectService.createRejectReason(campaignId, reasonText));
    }

    @GetMapping("/{campaignId}")
    @ApiMessage("Lấy danh sách lý do từ chối theo campaign")
    public ResponseEntity<List<CampaignRejectReasonDTO>> getRejectReasons(@PathVariable("campaignId") Long campaignId) {
        return ResponseEntity.ok(rejectService.getRejectReasons(campaignId));
    }

    @PostMapping("/message/{reasonId}")
    @ApiMessage("Thêm phản hồi chat cho lý do từ chối")
    public ResponseEntity<CampaignRejectMessageDTO> addMessage(
            @PathVariable("reasonId") Long reasonId,
            @RequestParam("senderId") Long senderId,
            @RequestBody String content) {
        return ResponseEntity.ok(rejectService.addMessage(reasonId, senderId, content));
    }

    @GetMapping("/message/{reasonId}")
    @ApiMessage("Lấy danh sách phản hồi chat theo lý do từ chối")
    public ResponseEntity<List<CampaignRejectMessageDTO>> getMessages(@PathVariable("reasonId") Long reasonId) {
        return ResponseEntity.ok(rejectService.getMessages(reasonId));
    }
}