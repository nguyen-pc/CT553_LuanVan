package com.vn.beta_testing.shop_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.beta_testing.domain.Clinic;
import com.vn.beta_testing.domain.request.clinic.ReqSignUpClinicDTO;
import com.vn.beta_testing.shop_service.service.ClinicService;
import com.vn.beta_testing.util.error.IdInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class ClinicController {
    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping("/signUpClinics")
    public ResponseEntity<Clinic> createClinic(@RequestBody Clinic clinic) {
        boolean isEmailExist = this.clinicService.isEmailExist(clinic.getClinicEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + clinic.getClinicEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
        }

        Clinic createdClinic = this.clinicService.handleCreateClinic(clinic);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClinic);
    }

}
