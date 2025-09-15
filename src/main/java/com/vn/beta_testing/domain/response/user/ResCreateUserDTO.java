package com.vn.beta_testing.domain.response.user;

import java.time.Instant;
import java.util.List;

import com.vn.beta_testing.util.constant.GenderEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
    private CompanyProfile companyProfile;

    @Setter
    @Getter
    public static class CompanyProfile {
        private long id;
        private String name;
    }
}
