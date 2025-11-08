package com.vn.beta_testing.feature.auth_service.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChangePasswordDTO {
    private Long id;
    private String oldPassword;
    private String newPassword;
}