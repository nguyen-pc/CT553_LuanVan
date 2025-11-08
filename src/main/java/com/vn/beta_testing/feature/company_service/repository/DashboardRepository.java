package com.vn.beta_testing.feature.company_service.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepository {

    @PersistenceContext
    private EntityManager em;

    // ============================================================
    // 📊 1️⃣ Biểu đồ: Bug theo trạng thái
    // ============================================================
    public List<Object[]> countBugsByStatus(Long companyId) {
        String sql = """
            SELECT b.status, COUNT(*) AS count
            FROM bug_reports b
            WHERE (:companyId IS NULL OR b.campaign_id IN (
                SELECT id FROM campaigns WHERE project_id IN (
                    SELECT id FROM projects WHERE company_id = :companyId
                )
            ))
            GROUP BY b.status
            ORDER BY b.status
        """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }

    // ============================================================
    // 📊 2️⃣ Biểu đồ: Campaign theo trạng thái
    // ============================================================
    public List<Object[]> countCampaignsByStatus(Long companyId) {
        String sql = """
            SELECT c.campaign_status, COUNT(*) AS count
            FROM campaigns c
            JOIN projects p ON c.project_id = p.id
            WHERE (:companyId IS NULL OR p.company_id = :companyId)
            GROUP BY c.campaign_status
            ORDER BY c.campaign_status
        """;

        Query query = em.createNativeQuery(sql);
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }

    // ============================================================
    // 📈 3️⃣ Tổng hợp số liệu
    // ============================================================
    public long countUsers(Long companyId) {
        String sql = """
            SELECT COUNT(*)
            FROM users
            WHERE company_id = :companyId
        """;
        Object result = em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getSingleResult();
        return ((Number) result).longValue();
    }

    public long countCompanies(Long companyId) {
        String sql = """
            SELECT COUNT(*)
            FROM company_profiles
            WHERE (:companyId IS NULL OR id = :companyId)
        """;
        Object result = em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getSingleResult();
        return ((Number) result).longValue();
    }

    public long countProjects(Long companyId) {
        String sql = """
            SELECT COUNT(*)
            FROM projects
            WHERE (:companyId IS NULL OR company_id = :companyId)
        """;
        Object result = em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getSingleResult();
        return ((Number) result).longValue();
    }

    public long countCampaigns(Long companyId) {
        String sql = """
            SELECT COUNT(*)
            FROM campaigns c
            JOIN projects p ON c.project_id = p.id
            WHERE (:companyId IS NULL OR p.company_id = :companyId)
        """;
        Object result = em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getSingleResult();
        return ((Number) result).longValue();
    }

    public long countBugs(Long companyId) {
        String sql = """
            SELECT COUNT(*)
            FROM bug_reports b
            WHERE (:companyId IS NULL OR b.campaign_id IN (
                SELECT id FROM campaigns WHERE project_id IN (
                    SELECT id FROM projects WHERE company_id = :companyId
                )
            ))
        """;
        Object result = em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getSingleResult();
        return ((Number) result).longValue();
    }

    // ============================================================
    // 🕓 4️⃣ Xu hướng 30 ngày gần nhất
    // ============================================================
    public List<Object[]> findUserTrend(Long companyId) {
        String sql = """
            SELECT DATE(created_at) AS date, COUNT(*) AS count
            FROM users
            WHERE company_id = :companyId
              AND created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY DATE(created_at)
            ORDER BY DATE(created_at)
        """;
        return em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getResultList();
    }

    public List<Object[]> findCompanyTrend(Long companyId) {
        String sql = """
            SELECT DATE(created_at) AS date, COUNT(*) AS count
            FROM company_profiles
            WHERE (:companyId IS NULL OR id = :companyId)
              AND created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY DATE(created_at)
            ORDER BY DATE(created_at)
        """;
        return em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getResultList();
    }

    public List<Object[]> findProjectTrend(Long companyId) {
        String sql = """
            SELECT DATE(created_at) AS date, COUNT(*) AS count
            FROM projects
            WHERE (:companyId IS NULL OR company_id = :companyId)
              AND created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY DATE(created_at)
            ORDER BY DATE(created_at)
        """;
        return em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getResultList();
    }

    public List<Object[]> findCampaignTrend(Long companyId) {
        String sql = """
            SELECT DATE(c.created_at) AS date, COUNT(*) AS count
            FROM campaigns c
            JOIN projects p ON c.project_id = p.id
            WHERE (:companyId IS NULL OR p.company_id = :companyId)
              AND c.created_at >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            GROUP BY DATE(c.created_at)
            ORDER BY DATE(c.created_at)
        """;
        return em.createNativeQuery(sql)
                .setParameter("companyId", companyId)
                .getResultList();
    }
}
