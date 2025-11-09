package com.vn.beta_testing.feature.company_service.DTO;


import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String description;
    private boolean status;
    private Date startDate;
    private Date endDate;
    private String bannerUrl;
    private String companyName;
    private String createdBy;
}
