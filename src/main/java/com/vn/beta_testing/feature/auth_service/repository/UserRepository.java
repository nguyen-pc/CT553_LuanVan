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

    // Admin Dashboard Queries

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    long countAllUsers();

    @Query(value = "SELECT COUNT(*) FROM users WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)", nativeQuery = true)
    long countUsersLast30Days();

    @Query(value = """
            SELECT COUNT(*) FROM users
            WHERE created_at BETWEEN DATE_SUB(CURDATE(), INTERVAL 60 DAY) AND DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            """, nativeQuery = true)
    long countUsersPrev30Days();

    @Query(value = """
                SELECT DATE(created_at) AS date, COUNT(*) AS count
                FROM users
                WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                GROUP BY DATE(created_at)
                ORDER BY DATE(created_at)
            """, nativeQuery = true)
    List<Object[]> findUserTrendLast30Days();

    @Query(value = "SELECT * FROM users ORDER BY created_at DESC LIMIT 10", nativeQuery = true)
    List<User> findTop10NewestUsers();

}
