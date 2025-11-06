package com.vn.beta_testing.feature.company_service.DTO;

import java.time.LocalDateTime;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectUserDTO {
    private Long id;
    private Long projectId;
    private String projectTitle;
    private Long userId;
    private String userName;
    private String role;
    private LocalDateTime joinedAt;
}