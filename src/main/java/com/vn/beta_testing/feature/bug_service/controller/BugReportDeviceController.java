package com.vn.beta_testing.feature.bug_service.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vn.beta_testing.domain.BugReportDevice;
import com.vn.beta_testing.feature.bug_service.service.BugReportDeviceService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/bugs/device")
public class BugReportDeviceController {

    private final BugReportDeviceService bugReportDeviceService;

    public BugReportDeviceController(BugReportDeviceService bugReportDeviceService) {
        this.bugReportDeviceService = bugReportDeviceService;
    }

    @PostMapping
    @ApiMessage("Create device info for BugReport")
    public ResponseEntity<BugReportDevice> createDevice(@RequestBody BugReportDevice device) {
        BugReportDevice created = bugReportDeviceService.createDevice(device);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{bugId}")
    @ApiMessage("Get all device info by BugReport ID")
    public ResponseEntity<List<BugReportDevice>> getDevices(@PathVariable("bugId") Long bugId) {
        return ResponseEntity.ok(bugReportDeviceService.getDevicesByBugId(bugId));
    }

    @DeleteMapping("/{deviceId}")
    @ApiMessage("Delete BugReportDevice by ID")
    public ResponseEntity<Void> deleteDevice(@PathVariable("deviceId") Long id) throws IdInvalidException {
        bugReportDeviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
