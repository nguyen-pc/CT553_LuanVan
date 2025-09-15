package com.vn.beta_testing.feature.company_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import com.vn.beta_testing.domain.CampaignType;

@Repository
public interface CampaignTypeRepository extends JpaRepository<CampaignType, Long>, JpaSpecificationExecutor<CampaignType> {
    
}