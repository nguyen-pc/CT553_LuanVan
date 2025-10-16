package com.vn.beta_testing.feature.bug_service.service;

import com.vn.beta_testing.domain.Attachment;
import com.vn.beta_testing.feature.bug_service.repository.AttachmentRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AttachmentService {
    private final AttachmentRepository repository;

    public AttachmentService(AttachmentRepository repository) {
        this.repository = repository;
    }

    // public Attachment uploadAttachment(Attachment attachment) {
    //     return repository.save(attachment);
    // }

    // public List<Attachment> getAttachments(Long bugId) {
    //     return repository.findByBugReportId(bugId);
    // }
}