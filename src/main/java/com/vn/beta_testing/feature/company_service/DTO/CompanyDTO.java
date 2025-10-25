package com.vn.beta_testing.feature.company_service.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class CompanyDTO {
    private Long id;
    private String companyName;
    private String companyEmail;
    private String companyAddress;
    private String companyPhoneNumber;
    private String companyWebsite;
    private String companyMST;
    private String companyAddressMST;
    private String description;
    private String logo;
    private String banner;
    private boolean active;
}