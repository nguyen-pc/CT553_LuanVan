package com.vn.beta_testing.feature.test_execution.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.TestCase;
import com.vn.beta_testing.domain.TestExecution;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.test_execution.DTO.TestExecutionDTO;
import com.vn.beta_testing.feature.test_execution.repository.TestExecutionRepository;
import com.vn.beta_testing.feature.testcase_service.service.TestCaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TestExecutionService {

    private final TestExecutionRepository testExecutionRepository;
    private final CampaignService campaignService;
    private final UserService userService;
    private final TestCaseService testCaseService;

    // 📦 Chuyển Entity -> DTO
    private TestExecutionDTO toDTO(TestExecution entity) {
        TestExecutionDTO dto = new TestExecutionDTO();
        dto.setId(entity.getId());
        dto.setNote(entity.getNote());
        dto.setStatus(entity.isStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCampaign() != null) {
            Campaign campaign = this.campaignService.fetchCampaignById(entity.getCampaign().getId());
            dto.setCampaignId(entity.getCampaign().getId());
            dto.setCampaignTitle(campaign != null ? campaign.getTitle() : null);
        }

        if (entity.getUser() != null) {
            User user = this.userService.fetchUserById(entity.getUser().getId());
            dto.setUserId(entity.getUser().getId());
            dto.setUserEmail(user != null ? user.getEmail() : null);
        }

        if (entity.getTestCase() != null) {
            TestCase testCase = this.testCaseService.fetchTestCaseById(entity.getTestCase().getId());
            dto.setTestCaseId(entity.getTestCase().getId());
            dto.setTestCaseTitle(testCase != null ? testCase.getTitle() : null);
        }

        return dto;
    }

    // ====== CRUD và truy vấn ======

    public List<TestExecutionDTO> getAll() {
        return testExecutionRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TestExecutionDTO> getById(Long id) {
        return testExecutionRepository.findById(id).map(this::toDTO);
    }

    public List<TestExecutionDTO> getByCampaign(Long campaignId) {
        return testExecutionRepository.findByCampaignId(campaignId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TestExecutionDTO> getByUser(Long userId) {
        return testExecutionRepository.findByUserId(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TestExecutionDTO> getByTestCase(Long testCaseId) {
        return testExecutionRepository.findByTestCaseId(testCaseId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TestExecutionDTO> getByCampaignAndUser(Long campaignId, Long userId) {
        return testExecutionRepository.findByCampaignIdAndUserId(campaignId, userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TestExecutionDTO> getByCampaignAndTestCase(Long campaignId, Long testCaseId) {
        return testExecutionRepository.findByCampaignIdAndTestCaseId(campaignId, testCaseId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TestExecutionDTO createOrUpdate(TestExecution testExecution) {
        // 🔍 Kiểm tra xem đã tồn tại record tương ứng (campaign + user + testCase) chưa
        Optional<TestExecution> existingOpt = testExecutionRepository
                .findByCampaignIdAndUserIdAndTestCaseId(
                        testExecution.getCampaign().getId(),
                        testExecution.getUser().getId(),
                        testExecution.getTestCase().getId());

        TestExecution saved;
        if (existingOpt.isPresent()) {
            // ✅ Nếu đã có -> cập nhật lại note & status
            TestExecution existing = existingOpt.get();
            existing.setNote(testExecution.getNote());
            existing.setStatus(testExecution.isStatus());
            existing.setUpdatedAt(Instant.now());
            saved = testExecutionRepository.save(existing);
        } else {
            // 🆕 Nếu chưa có -> tạo mới
            testExecution.setCreatedAt(Instant.now());
            testExecution.setUpdatedAt(Instant.now());
            saved = testExecutionRepository.save(testExecution);
        }

        return toDTO(saved);
    }

    public TestExecutionDTO update(Long id, TestExecution updated) {
        TestExecution saved = testExecutionRepository.findById(id)
                .map(existing -> {
                    existing.setNote(updated.getNote());
                    existing.setStatus(updated.isStatus());
                    existing.setCampaign(updated.getCampaign());
                    existing.setUser(updated.getUser());
                    existing.setTestCase(updated.getTestCase());
                    return testExecutionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("TestExecution not found"));
        return toDTO(saved);
    }

    public void delete(Long id) {
        testExecutionRepository.deleteById(id);
    }
}