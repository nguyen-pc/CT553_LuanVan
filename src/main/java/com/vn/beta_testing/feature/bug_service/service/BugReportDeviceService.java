package com.vn.beta_testing.feature.bug_service.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vn.beta_testing.domain.BugReportDevice;
import com.vn.beta_testing.feature.bug_service.repository.BugReportDeviceRepository;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
@Transactional
public class BugReportDeviceService {

    private final BugReportDeviceRepository deviceRepository;

    public BugReportDeviceService(BugReportDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public BugReportDevice createDevice(BugReportDevice device) {
        return deviceRepository.save(device);
    }

    public List<BugReportDevice> getDevicesByBugId(Long bugId) {
        return deviceRepository.findByBugId(bugId);
    }

    public void deleteDevice(Long id) throws IdInvalidException {
        BugReportDevice existing = deviceRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Device id = " + id + " not found"));
        deviceRepository.delete(existing);
    }
}
