package com.vn.beta_testing.feature.auth_service.DTO;


import com.vn.beta_testing.util.constant.GenderEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Long id;

    private Long userId;
    private String username;

    private String birthYear;
    private Integer age;
    private String zipcode;
    private String country;
    private String householdIncome;
    private Boolean children;
    private String employment;
    private String education;
    private String gamingGenres;
    private String browsers;
    private String webExpertise;
    private String language;
    private String computer;
    private String smartPhone;
    private String tablet;
    private String otherDevice;
    private GenderEnum gender;
}
