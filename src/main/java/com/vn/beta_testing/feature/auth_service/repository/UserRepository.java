package com.vn.beta_testing.feature.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.CompanyProfile;
import com.vn.beta_testing.domain.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);

    List<User> findByCompanyProfile_Id(Long companyId);

    @Query("SELECT u.companyProfile FROM User u WHERE u.id = :userId")
    CompanyProfile findCompanyByUserId(@Param("userId") Long userId);

}
