package com.vn.beta_testing.feature.reward.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vn.beta_testing.domain.UserPaymentInfo;

public interface UserPaymentInfoRepository extends JpaRepository<UserPaymentInfo, Long> {

}