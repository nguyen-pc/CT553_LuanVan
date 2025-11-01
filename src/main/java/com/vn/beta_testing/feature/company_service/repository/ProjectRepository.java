package com.vn.beta_testing.feature.company_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    // Admin Dashboard Queries

    @Query(value = "SELECT COUNT(*) FROM projects", nativeQuery = true)
    long countAllProjects();

    @Query(value = "SELECT COUNT(*) FROM projects WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)", nativeQuery = true)
    long countProjectsLast30Days();

    @Query(value = """
            SELECT COUNT(*) FROM projects
            WHERE created_at BETWEEN DATE_SUB(CURDATE(), INTERVAL 60 DAY) AND DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            """, nativeQuery = true)
    long countProjectsPrev30Days();

    @Query(value = """
                SELECT DATE(created_at) AS date, COUNT(*) AS count
                FROM projects
                WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                GROUP BY DATE(created_at)
                ORDER BY DATE(created_at)
            """, nativeQuery = true)
    List<Object[]> findProjectTrendLast30Days();
}