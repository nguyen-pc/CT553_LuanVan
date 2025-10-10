package com.vn.beta_testing.feature.company_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.vn.beta_testing.domain.UserRecruitProfile;

@Repository
public interface UserRecruitProfileRepository extends JpaRepository<UserRecruitProfile, Long>,
        JpaSpecificationExecutor<UserRecruitProfile> {

    UserRecruitProfile findByCampaignId(Long campaignId);
}