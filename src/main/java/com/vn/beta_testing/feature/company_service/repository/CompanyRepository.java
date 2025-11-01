package com.vn.beta_testing.feature.company_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.CompanyProfile;

@Repository
public interface CompanyRepository
                extends JpaRepository<CompanyProfile, Long>, JpaSpecificationExecutor<CompanyProfile> {
        boolean existsByCompanyEmail(String email);

        // Additional custom query methods can be defined here

        @Query(value = "SELECT COUNT(*) FROM company_profiles", nativeQuery = true)
        long countAllCompanies();

        @Query(value = "SELECT COUNT(*) FROM company_profiles WHERE updated_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)", nativeQuery = true)
        long countCompaniesLast30Days();

        @Query(value = """
                        SELECT COUNT(*) FROM company_profiles
                        WHERE updated_at BETWEEN DATE_SUB(CURDATE(), INTERVAL 60 DAY) AND DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                        """, nativeQuery = true)
        long countCompaniesPrev30Days();

        @Query(value = """
                            SELECT DATE(updated_at) AS date, COUNT(*) AS count
                            FROM company_profiles
                            WHERE updated_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                            GROUP BY DATE(updated_at)
                            ORDER BY DATE(updated_at)
                        """, nativeQuery = true)
        List<Object[]> findCompanyTrendLast30Days();

        @Query(value = "SELECT * FROM company_profiles ORDER BY updated_at DESC LIMIT 10", nativeQuery = true)
        List<CompanyProfile> findTop10NewestCompanies();
}