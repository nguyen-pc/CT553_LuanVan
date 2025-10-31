package com.vn.beta_testing.feature.company_service.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.constant.CampaignStatus;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CampaignController {
    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/project/{projectId}/campaigns")
    @ApiMessage("Get all campaigns")
    public ResponseEntity<ResultPaginationDTO> getAllCampaignsByProject(
            @PathVariable("projectId") long projectId,
            @Filter Specification<Campaign> spec,
            Pageable pageable) {
        // Tạo specification lọc theo projectId
        Specification<Campaign> projectSpec = (root, query, builder) -> builder.equal(root.get("project").get("id"),
                projectId);

        Specification<Campaign> finalSpec = Specification.where(projectSpec).and(spec);
        return ResponseEntity.ok().body(this.campaignService.fetchAll(finalSpec, pageable));
    }

    @GetMapping("/campaigns")
    @ApiMessage("Get all campaigns successfully")
    public ResponseEntity<ResultPaginationDTO> getAllCampaigns(
            @Filter Specification<Campaign> spec,
            Pageable pageable,
            @RequestParam(value = "campaignStatus", required = false) String campaignStatus) {

        Specification<Campaign> notDraftSpec = (root, query, cb) -> cb.isFalse(root.get("isDraft"));

        Specification<Campaign> statusSpec = null;
        if (campaignStatus != null && !campaignStatus.isEmpty()) {
            statusSpec = (root, query, cb) -> cb.equal(root.get("campaignStatus"), campaignStatus);
        }

        Specification<Campaign> finalSpec = Specification
                .where(notDraftSpec)
                .and(spec)
                .and(statusSpec);

        // ⚙️ 4️⃣ Gọi service fetchAll
        ResultPaginationDTO result = this.campaignService.fetchAll(finalSpec, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/campaign/{id}")
    @ApiMessage("Get campaign by id")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable("id") Long id) {
        Campaign campaign = this.campaignService.fetchCampaignById(id);
        if (campaign == null) {
            throw new IdInvalidException("Campaign with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/campaign/create")
    @ApiMessage("Create a new campaign")
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        Campaign createdCampaign = campaignService.createCampaign(campaign);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCampaign);
    }

    @PutMapping("/campaign/update/{id}")
    @ApiMessage("Update campaign")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable("id") Long id, @RequestBody Campaign campaign) {
        long idCampaign = campaign.getId();
        Campaign existingCampaign = this.campaignService.fetchCampaignById(idCampaign);
        if (existingCampaign == null) {
            throw new IdInvalidException("Campaign with id = " + id + " does not exist.");
        }

        Campaign updatedCampaign = this.campaignService.updateCampaign(campaign);
        return ResponseEntity.ok(updatedCampaign);
    }

    @PutMapping("/campaign/{id}/publish")
    @ApiMessage("Publish campaign successfully")
    public ResponseEntity<Campaign> publishCampaign(@PathVariable("id") Long id) {
        Campaign updated = campaignService.publishCampaign(id);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/campaign/{id}/status")
    @ApiMessage("Update campaign status successfully")
    public ResponseEntity<Campaign> updateCampaignStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String newStatus) {

        Campaign campaign = campaignService.fetchCampaignById(id);
        if (campaign == null) {
            throw new IdInvalidException("Campaign with id = " + id + " does not exist.");
        }

        campaign.setCampaignStatus(CampaignStatus.valueOf(newStatus));
        Campaign updated = campaignService.updateCampaign(campaign);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/campaigns/active-approved")
    @ApiMessage("Get all approved campaigns that have not ended yet")
    public ResponseEntity<ResultPaginationDTO> getApprovedAndActiveCampaigns(
            @Filter Specification<Campaign> spec,
            Pageable pageable) {

        // Trạng thái phải là APPROVED
        Specification<Campaign> approvedSpec = (root, query, cb) -> cb.equal(root.get("campaignStatus"),
                CampaignStatus.APPROVED);

        // endDate > hiện tại
        Specification<Campaign> notEndedSpec = (root, query, cb) -> cb.greaterThan(root.get("endDate"),
                java.time.Instant.now());

        // Không phải draft
        Specification<Campaign> notDraftSpec = (root, query, cb) -> cb.isFalse(root.get("isDraft"));

        // Kết hợp tất cả các điều kiện
        Specification<Campaign> finalSpec = Specification
                .where(approvedSpec)
                .and(notEndedSpec)
                .and(notDraftSpec)
                .and(spec);

        ResultPaginationDTO result = campaignService.fetchAll(finalSpec, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/campaigns/upcoming")
    @ApiMessage("Get all upcoming campaigns that have not started yet")
    public ResponseEntity<ResultPaginationDTO> getUpcomingCampaigns(
            @Filter Specification<Campaign> spec,
            Pageable pageable) {

        // Trạng thái đã được duyệt
        Specification<Campaign> approvedSpec = (root, query, cb) -> cb.equal(root.get("campaignStatus"),
                CampaignStatus.APPROVED);

        // startDate > hiện tại → chưa bắt đầu
        Specification<Campaign> upcomingSpec = (root, query, cb) -> cb.greaterThan(root.get("startDate"),
                java.time.Instant.now());

        // Không phải bản nháp
        Specification<Campaign> notDraftSpec = (root, query, cb) -> cb.isFalse(root.get("isDraft"));

        // Gộp điều kiện
        Specification<Campaign> finalSpec = Specification
                .where(approvedSpec)
                .and(upcomingSpec)
                .and(notDraftSpec)
                .and(spec);

        ResultPaginationDTO result = campaignService.fetchAll(finalSpec, pageable);
        return ResponseEntity.ok(result);
    }

}
