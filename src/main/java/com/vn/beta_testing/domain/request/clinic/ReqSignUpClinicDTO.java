package com.vn.beta_testing.domain.request.clinic;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqSignUpClinicDTO {
    @NotBlank(message = "clinicName can not empty")
    private String clinicName;
    @NotBlank(message = "clinicEmail can not empty")
    private String clinicEmail;
    @NotBlank(message = "clinicPhoneNumber can not empty")
    private String clinicPhoneNumber;
    @NotBlank(message = "clinicAddress can not empty")
    private String clinicAddress;
    @NotBlank(message = "clinicMST can not empty")
    private String clinicMST;
    @NotBlank(message = "clinicAddressMST can not empty")
    private String clinicAddressMST;
    @NotBlank(message = "clinicDateMST can not empty")
    private String clinicDateMST;

    private boolean active;
}
