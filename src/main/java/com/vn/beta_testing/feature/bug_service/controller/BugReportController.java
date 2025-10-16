package com.vn.beta_testing.feature.bug_service.controller;

import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.bug_service.DTO.BugReportDTO;
import com.vn.beta_testing.feature.bug_service.service.BugReportService;
import com.vn.beta_testing.util.constant.PriorityEnum;
import com.vn.beta_testing.util.constant.SeverityEnum;
import com.vn.beta_testing.util.constant.StatusBugEnum;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bugs")
public class BugReportController {

    private final BugReportService service;

    public BugReportController(BugReportService service) {
        this.service = service;
    }

    @PostMapping
    public BugReportDTO create(@RequestBody BugReportDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<BugReportDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BugReportDTO getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public BugReportDTO update(@PathVariable("id") Long id, @RequestBody BugReportDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @GetMapping("/by-tester/{testerId}")
    public List<BugReportDTO> getByTester(@PathVariable("testerId") Long testerId) {
        return service.getByTester(testerId);
    }

    @GetMapping("/by-assignee/{assigneeId}")
    public List<BugReportDTO> getByAssignee(@PathVariable("assigneeId") Long assigneeId) {
        return service.getByAssignee(assigneeId);
    }

    @GetMapping("/by-type/{bugTypeId}")
    public List<BugReportDTO> getByBugType(@PathVariable("bugTypeId") Long bugTypeId) {
        return service.getByBugType(bugTypeId);
    }

    @GetMapping("/by-campaign/{campaignId}")
    public List<BugReportDTO> getByCampaign(@PathVariable("campaignId") Long campaignId) {
        return service.getByCampaign(campaignId);
    }

    @GetMapping("/filter")
    public ResultPaginationDTO filter(
            @RequestParam(name = "testerId", required = false) Long testerId,
            @RequestParam(name = "assigneeId", required = false) Long assigneeId,
            @RequestParam(name = "bugTypeId", required = false) Long bugTypeId,
            @RequestParam(name = "campaignId", required = false) Long campaignId,
            @RequestParam(name = "status", required = false) StatusBugEnum status,
            @RequestParam(name = "priority", required = false) PriorityEnum priority,
            @RequestParam(name = "severity", required = false) SeverityEnum severity,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return service.filterWithPagination(
                testerId, assigneeId, bugTypeId, campaignId,
                status, priority, severity, pageable);
    }
}