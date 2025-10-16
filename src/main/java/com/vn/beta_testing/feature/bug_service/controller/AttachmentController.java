package com.vn.beta_testing.feature.bug_service.controller;

import com.vn.beta_testing.domain.Attachment;
import com.vn.beta_testing.feature.bug_service.service.AttachmentService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attachments")
public class AttachmentController {

    private final AttachmentService service;

    public AttachmentController(AttachmentService service) {
        this.service = service;
    }

    // @PostMapping
    // public Attachment upload(@RequestBody Attachment attachment) {
    //     return service.uploadAttachment(attachment);
    // }

    // @GetMapping("/bug/{bugId}")
    // public List<Attachment> getAttachments(@PathVariable Long bugId) {
    //     return service.getAttachments(bugId);
    // }
}