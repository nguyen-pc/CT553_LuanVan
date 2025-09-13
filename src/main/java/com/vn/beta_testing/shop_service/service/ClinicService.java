package com.vn.beta_testing.shop_service.service;

import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.Clinic;
import com.vn.beta_testing.shop_service.repository.ClinicRepository;

@Service
public class ClinicService {
    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public boolean isEmailExist(String email) {
        return this.clinicRepository.existsByClinicEmail(email);
    }

    public Clinic handleCreateClinic(Clinic clinic) {
        return this.clinicRepository.save(clinic);
    }
}
