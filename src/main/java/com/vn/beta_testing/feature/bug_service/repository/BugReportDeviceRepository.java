package com.vn.beta_testing.feature.bug_service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vn.beta_testing.domain.BugReportDevice;

@Repository
public interface BugReportDeviceRepository extends JpaRepository<BugReportDevice, Long> {

    List<BugReportDevice> findByBugId(Long bugId);
}