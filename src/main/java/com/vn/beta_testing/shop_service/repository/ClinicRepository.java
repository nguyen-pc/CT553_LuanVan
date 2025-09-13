package com.vn.beta_testing.shop_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Clinic;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long>, JpaSpecificationExecutor<Clinic> {
    Clinic findByClinicEmail(String clinicEmail);

    boolean existsByClinicEmail(String clinicEmail);

}
