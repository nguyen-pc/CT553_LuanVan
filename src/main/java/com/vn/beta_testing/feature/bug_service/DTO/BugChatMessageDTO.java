package com.vn.beta_testing.feature.bug_service.DTO;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class BugChatMessageDTO {
    private Long id;
    private String content;
    private boolean isInternal;
    private LocalDateTime createdAt;

    private Long senderId;
    private String senderEmail;
    private String senderName;

    private Long bugId;
}
