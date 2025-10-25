package com.vn.beta_testing.feature.company_service.repository;

import com.vn.beta_testing.domain.TesterInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TesterInviteRepository extends JpaRepository<TesterInvite, Long> {
    List<TesterInvite> findByCampaignId(Long campaignId);
}