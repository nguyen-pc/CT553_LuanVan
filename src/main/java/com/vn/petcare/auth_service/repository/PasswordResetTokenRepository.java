package com.vn.petcare.auth_service.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vn.petcare.domain.PasswordResetToken;
import com.vn.petcare.domain.User;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByUser(User user);
}
