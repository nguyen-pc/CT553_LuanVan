package com.vn.beta_testing.feature.email_service.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountEmailRequest {
    private String to;
    private String username;
    private String password;
}