package com.vn.beta_testing.feature.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>,
                JpaSpecificationExecutor<Role> {
        boolean existsByName(String name);

        Role findByName(String name);
}