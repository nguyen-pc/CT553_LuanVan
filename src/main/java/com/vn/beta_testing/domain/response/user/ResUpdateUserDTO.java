package com.vn.beta_testing.domain.response.user;

import java.time.Instant;

import com.vn.beta_testing.util.constant.GenderEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Clinic clinic;

    @Setter
    @Getter
    public static class Clinic {
        private long id;
        private String name;

    }
}
