package com.vn.beta_testing.feature.auth_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.beta_testing.domain.PasswordResetToken;
import com.vn.beta_testing.domain.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);
}
