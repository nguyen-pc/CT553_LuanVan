package com.vn.beta_testing.feature.company_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.beta_testing.domain.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findByProjectId(Long projectId);

    @Query("SELECT c.module FROM Campaign c WHERE c.id = :campaignId")
    Optional<Module> findModuleByCampaignId(@Param("campaignId") Long campaignId);
}