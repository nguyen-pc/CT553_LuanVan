package com.vn.beta_testing.feature.survey_service.DTO;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {
    private long fileId;
    private String fileName;
    private String fileType;
    private long fileSize;
    private Instant createdAt;
    private Instant updatedAt;

    private Long uploaderId;
    private String uploaderName;
    private String uploaderEmail;
}