package com.vn.beta_testing.feature.bug_service.DTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    private Long uploaderId;
    private String uploaderName;
    private String uploaderEmail;

    private Long campaignId;
    private String campaignName;
}