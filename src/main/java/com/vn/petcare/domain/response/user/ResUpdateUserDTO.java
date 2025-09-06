package com.vn.petcare.domain.response.user;


import java.time.Instant;

import com.vn.petcare.util.constant.GenderEnum;

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
