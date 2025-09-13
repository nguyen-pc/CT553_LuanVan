package com.vn.beta_testing.domain.response.user;

import java.time.Instant;

import com.vn.beta_testing.util.constant.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String email;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private Instant createdAt;

    private Clinic clinic;

    private RoleUser role;

    // private SkillUser[] skill;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Clinic {
        private long id;
        private String name;
    }

    // @Getter
    // @Setter
    // @AllArgsConstructor
    // @NoArgsConstructor
    // public static class SkillUser {
    // private long id;
    // private String name;
    // }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }
}
