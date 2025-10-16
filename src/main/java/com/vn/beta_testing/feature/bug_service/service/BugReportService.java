package com.vn.beta_testing.feature.bug_service.service;

import com.vn.beta_testing.domain.BugReport;
import com.vn.beta_testing.domain.BugType;
import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.bug_service.DTO.BugReportDTO;
import com.vn.beta_testing.feature.bug_service.controller.mapper.BugReportMapper;
import com.vn.beta_testing.feature.bug_service.repository.BugReportRepository;
import com.vn.beta_testing.util.constant.PriorityEnum;
import com.vn.beta_testing.util.constant.SeverityEnum;
import com.vn.beta_testing.util.constant.StatusBugEnum;

import static com.vn.beta_testing.feature.bug_service.repository.BugReportSpecification.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BugReportService {

    private final BugReportRepository repository;

    public BugReportService(BugReportRepository repository) {
        this.repository = repository;
    }

    public BugReportDTO create(BugReportDTO dto) {
        BugReport bug = BugReportMapper.toEntity(dto);
        BugReport saved = repository.save(bug);
        return BugReportMapper.toDTO(saved);
    }

    public List<BugReportDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(BugReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BugReportDTO getById(Long id) {
        return repository.findById(id)
                .map(BugReportMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("BugReport not found with id: " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public BugReportDTO update(Long id, BugReportDTO dto) {
        BugReport existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("BugReport not found with id: " + id));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setSeverity(dto.getSeverity());
        existing.setPriority(dto.getPriority());
        existing.setStatus(dto.getStatus());
        existing.setStepsToReproduce(dto.getStepsToReproduce());
        existing.setExpectedResult(dto.getExpectedResult());
        existing.setActualResult(dto.getActualResult());

        if (dto.getTesterId() != null) {
            User tester = new User();
            tester.setId(dto.getTesterId());
            existing.setTester(tester);
        }

        if (dto.getAssigneeId() != null) {
            User assignee = new User();
            assignee.setId(dto.getAssigneeId());
            existing.setAssignee(assignee);
        }

        if (dto.getBugTypeId() != null) {
            BugType type = new BugType();
            type.setId(dto.getBugTypeId());
            existing.setBugType(type);
        }

        if (dto.getCampaignId() != null) {
            Campaign campaign = new Campaign();
            campaign.setId(dto.getCampaignId());
            existing.setCampaign(campaign);
        }

        BugReport updated = repository.save(existing);
        return BugReportMapper.toDTO(updated);
    }

    public List<BugReportDTO> getByTester(Long testerId) {
        return repository.findByTester_Id(testerId)
                .stream().map(BugReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BugReportDTO> getByAssignee(Long assigneeId) {
        return repository.findByAssignee_Id(assigneeId)
                .stream().map(BugReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BugReportDTO> getByBugType(Long bugTypeId) {
        return repository.findByBugType_Id(bugTypeId)
                .stream().map(BugReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<BugReportDTO> getByCampaign(Long campaignId) {
        return repository.findByCampaign_Id(campaignId)
                .stream().map(BugReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ResultPaginationDTO filterWithPagination(
            Long testerId,
            Long assigneeId,
            Long bugTypeId,
            Long campaignId,
            StatusBugEnum status,
            PriorityEnum priority,
            SeverityEnum severity,
            Pageable pageable) {
        Specification<BugReport> spec = Specification
                .where(hasTester(testerId))
                .and(hasAssignee(assigneeId))
                .and(hasBugType(bugTypeId))
                .and(hasCampaign(campaignId))
                .and(hasStatus(status))
                .and(hasPriority(priority))
                .and(hasSeverity(severity));

        Page<BugReport> page = repository.findAll(spec, pageable);

        List<?> dtoList = page.getContent()
                .stream()
                .map(BugReportMapper::toDTO)
                .toList();

        // Gói pagination meta
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(dtoList);

        return result;
    }

}