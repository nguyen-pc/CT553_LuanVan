package com.vn.beta_testing.feature.censor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vn.beta_testing.domain.CampaignRejectMessage;

public interface CampaignRejectMessageRepository extends JpaRepository<CampaignRejectMessage, Long> {
    List<CampaignRejectMessage> findByRejectReasonIdOrderByCreatedAtAsc(Long rejectReasonId);
}