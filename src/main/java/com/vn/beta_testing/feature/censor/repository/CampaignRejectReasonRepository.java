package com.vn.beta_testing.feature.censor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vn.beta_testing.domain.CampaignRejectReason;

public interface CampaignRejectReasonRepository extends JpaRepository<CampaignRejectReason, Long> {
    List<CampaignRejectReason> findByCampaignIdOrderByCreatedAtDesc(Long campaignId);
}
