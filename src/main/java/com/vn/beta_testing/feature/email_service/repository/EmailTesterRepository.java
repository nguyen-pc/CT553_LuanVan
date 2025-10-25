package com.vn.beta_testing.feature.email_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vn.beta_testing.domain.EmailTester;

public interface EmailTesterRepository extends JpaRepository<EmailTester, Long> {
    List<EmailTester> findByCampaignId(Long campaignId);

    boolean existsByEmailAndCampaignId(String email, Long campaignId);
}