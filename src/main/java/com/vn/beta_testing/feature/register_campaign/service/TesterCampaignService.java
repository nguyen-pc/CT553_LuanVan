package com.vn.beta_testing.feature.register_campaign.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vn.beta_testing.domain.TesterCampaign;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.UserProfile;
import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.feature.company_service.mapDTO.CampaignMapper;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.register_campaign.DTO.response.CompletionDailyDTO;
import com.vn.beta_testing.feature.register_campaign.DTO.response.GetTesterCampaignDTO;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterCampaignDTO;
import com.vn.beta_testing.feature.register_campaign.DTO.response.TesterCampaignStatsDTO;
import com.vn.beta_testing.feature.register_campaign.DTO.response.UserTesterProfileDTO;
import com.vn.beta_testing.feature.register_campaign.repository.TesterCampaignRepository;
import com.vn.beta_testing.util.constant.TesterCampaignStatus;
import com.vn.beta_testing.util.error.IdInvalidException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TesterCampaignService {

    private final TesterCampaignRepository testerCampaignRepository;
    private final CampaignService campaignService;
    private final UserService userService;

    public TesterCampaignService(
            TesterCampaignRepository testerCampaignRepository,
            CampaignService campaignService,
            UserService userService) {
        this.testerCampaignRepository = testerCampaignRepository;
        this.campaignService = campaignService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public ResultPaginationDTO fetchByCampaignWithSpec(Long campaignId, Specification<TesterCampaign> spec,
            Pageable pageable) {

        // filter thêm campaignId vào spec
        Specification<TesterCampaign> campaignSpec = (root, query, cb) -> cb.equal(root.get("campaign").get("id"),
                campaignId);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String statusParam = request.getParameter("status");

        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                // ✅ Chuyển String -> Enum đúng kiểu
                TesterCampaignStatus enumStatus = TesterCampaignStatus.valueOf(statusParam.toUpperCase());
                Specification<TesterCampaign> statusSpec = (root, query, cb) -> cb.equal(root.get("status"),
                        enumStatus);

                campaignSpec = campaignSpec.and(statusSpec);
            } catch (IllegalArgumentException e) {
                // Nếu param không khớp Enum, có thể log hoặc bỏ qua
                System.out.println("⚠️ Invalid status value: " + statusParam);
            }
        }

        Specification<TesterCampaign> combinedSpec = spec == null ? campaignSpec : spec.and(campaignSpec);

        // JOIN FETCH để lấy luôn user và userProfile trong 1 query
        queryFetchUserAndProfile(combinedSpec);

        Page<TesterCampaign> page = testerCampaignRepository.findAll(combinedSpec, pageable);

        List<GetTesterCampaignDTO> dtos = page.getContent().stream().map(this::toTesterCampaignDTO).toList();

        // gói vào ResultPaginationDTO
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);
        result.setResult(dtos);

        return result;
    }

    private void queryFetchUserAndProfile(Specification<TesterCampaign> spec) {
        // có thể tùy chỉnh thêm trong repository nếu cần query tùy chỉnh
    }

    private GetTesterCampaignDTO toTesterCampaignDTO(TesterCampaign tc) {
        GetTesterCampaignDTO dto = new GetTesterCampaignDTO();
        dto.setId(tc.getId());
        dto.setStatus(tc.getStatus() != null ? tc.getStatus().name() : null);
        dto.setJoinDate(tc.getJoinDate());

        User user = tc.getUser();
        if (user != null && user.getId() != 0) {
            UserTesterProfileDTO profileDTO = new UserTesterProfileDTO();
            profileDTO.setUserId(user.getId());
            profileDTO.setName(user.getName());

            UserProfile profile = user.getUserProfile(); // nếu có mappedBy bên User
            if (profile != null) {
                profileDTO.setGender(profile.getGender() != null ? profile.getGender().name() : null);
                profileDTO.setLocation(profile.getZipcode() + " - " + profile.getCountry());
                profileDTO.setEducation(profile.getEducation());
                profileDTO.setIncome(profile.getHouseholdIncome());
                profileDTO.setDevices(getDevices(profile));

            }

            dto.setUser(profileDTO);
        }
        return dto;
    }

    private String getDevices(UserProfile p) {
        List<String> devices = new ArrayList<>();
        if (p.getComputer() != null && !p.getComputer().isBlank())
            devices.add("PC");
        if (p.getSmartPhone() != null && !p.getSmartPhone().isBlank())
            devices.add("iOS/Android");
        if (p.getTablet() != null && !p.getTablet().isBlank())
            devices.add("Tablet");
        if (p.getOtherDevice() != null && !p.getOtherDevice().isBlank())
            devices.add("Other");
        return String.join(", ", devices);
    }

    /** Get all testers by filter */
    public ResultPaginationDTO fetchAll(Specification<TesterCampaign> spec, Pageable pageable) {
        Page<TesterCampaign> page = this.testerCampaignRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(page.getContent());
        return rs;
    }

    /** Get testers by campaign */
    public List<TesterCampaign> getTestersByCampaign(Long campaignId) {
        return testerCampaignRepository.findByCampaign_Id(campaignId);
    }

    public TesterCampaignDTO toDTO(TesterCampaign entity) {
        if (entity == null)
            return null;

        return TesterCampaignDTO.builder()
                .id(entity.getId())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .progress(entity.getProgress())
                .note(entity.getNote())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .joinDate(entity.getJoinDate())
                .isUpload(entity.isUpload())
                .uploadLink(entity.getUploadLink())
                .campaign(CampaignMapper.toDTO(entity.getCampaign())) // 👈 ánh xạ campaign
                .build();
    }

    public List<TesterCampaign> getCampaignsByUser(Long userId) {
        return testerCampaignRepository.findByUser_Id(userId);
    }

    /** Apply for campaign */
    public TesterCampaign applyForCampaign(Long userId, Long campaignId, String note) {
        if (testerCampaignRepository.existsByUser_IdAndCampaign_Id(userId, campaignId)) {
            throw new IdInvalidException("User already applied for this campaign.");
        }

        Campaign campaign = campaignService.fetchCampaignById(campaignId);
        if (campaign == null)
            throw new IdInvalidException("Campaign not found.");

        User user = userService.fetchUserById(userId);
        if (user == null)
            throw new IdInvalidException("User not found.");

        TesterCampaign tester = new TesterCampaign();
        tester.setUser(user);
        tester.setCampaign(campaign);
        tester.setStatus(TesterCampaignStatus.PENDING);
        tester.setJoinDate(Instant.now());
        tester.setNote(note);

        return testerCampaignRepository.save(tester);
    }

    /** Approve tester */
    public TesterCampaign approveTester(Long testerCampaignId) {
        TesterCampaign tester = testerCampaignRepository.findById(testerCampaignId)
                .orElseThrow(() -> new IdInvalidException("Tester not found"));
        tester.setStatus(TesterCampaignStatus.APPROVED);
        return testerCampaignRepository.save(tester);
    }

    /** Reject tester */
    public TesterCampaign rejectTester(Long testerCampaignId) {
        TesterCampaign tester = testerCampaignRepository.findById(testerCampaignId)
                .orElseThrow(() -> new IdInvalidException("Tester not found"));
        tester.setStatus(TesterCampaignStatus.REJECTED);
        return testerCampaignRepository.save(tester);
    }

    /** Update progress */
    public TesterCampaign updateProgress(Long testerCampaignId, int progress) {
        TesterCampaign tester = testerCampaignRepository.findById(testerCampaignId)
                .orElseThrow(() -> new IdInvalidException("Tester not found"));

        tester.setProgress(progress);
        if (progress >= 100) {
            tester.setStatus(TesterCampaignStatus.COMPLETED);
            tester.setCompletionDate(Instant.now());
        } else {
            tester.setStatus(TesterCampaignStatus.IN_PROGRESS);
        }

        return testerCampaignRepository.save(tester);
    }

    public TesterCampaignStatsDTO getStatsByCampaign(Long campaignId) {
        TesterCampaignStatsDTO dto = new TesterCampaignStatsDTO();

        dto.setAccepted(testerCampaignRepository.countByCampaignIdAndStatus(campaignId, TesterCampaignStatus.APPROVED));
        dto.setRejected(testerCampaignRepository.countByCampaignIdAndStatus(campaignId, TesterCampaignStatus.REJECTED));
        dto.setPending(testerCampaignRepository.countByCampaignIdAndStatus(campaignId, TesterCampaignStatus.PENDING));
        dto.setApplied(testerCampaignRepository.countByCampaignId(campaignId));
        return dto;
    }

    public TesterCampaign getTesterCampaign(Long userId, Long campaignId) {
        return testerCampaignRepository
                .findByUserIdAndCampaignId(userId, campaignId)
                .orElse(null);
    }

    public Map<String, Object> getTesterCampaignStatus(Long userId, Long campaignId) {
        Optional<TesterCampaign> optional = testerCampaignRepository
                .findByUserIdAndCampaignId(userId, campaignId);

        Map<String, Object> response = new HashMap<>();

        if (optional.isPresent()) {
            TesterCampaign testerCampaign = optional.get();
            response.put("exists", true);
            response.put("status", testerCampaign.getStatus());
            response.put("note", testerCampaign.getNote());
        } else {
            response.put("exists", false);
            response.put("status", "NOT_REGISTERED");
        }

        return response;
    }

    public TesterCampaign markUploadedByUserAndCampaign(Long userId, Long campaignId, String fileName) {
        TesterCampaign testerCampaign = testerCampaignRepository
                .findByUserIdAndCampaignId(userId, campaignId)
                .orElseThrow(() -> new RuntimeException(
                        "TesterCampaign not found for userId=" + userId + " and campaignId=" + campaignId));

        testerCampaign.setUpload(true);
        testerCampaign.setUploadLink("http://localhost:8081/storage/" + campaignId + "/" + fileName);
        testerCampaign.setCompletionDate(Instant.now());
        testerCampaign.setProgress(50);

        return testerCampaignRepository.save(testerCampaign);
    }

    public List<CompletionDailyDTO> getCompletionStats(Long campaignId) {
        List<Object[]> result = testerCampaignRepository.getCompletionStatsByDate(campaignId);
        return result.stream()
                .map(r -> new CompletionDailyDTO(
                        ((java.sql.Date) r[0]).toLocalDate(),
                        ((Number) r[1]).longValue()))
                .collect(Collectors.toList());
    }
}
