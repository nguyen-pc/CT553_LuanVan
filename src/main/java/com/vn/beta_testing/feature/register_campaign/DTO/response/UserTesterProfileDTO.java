package com.vn.beta_testing.feature.register_campaign.DTO.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTesterProfileDTO {
    private Long userId;
    private String name;
    private String gender;
    private String location;     
    private String education;
    private String income;
    private String devices;      

}
