    package com.vn.beta_testing.feature.company_service.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.vn.beta_testing.domain.UserRecruitProfile;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.company_service.mapDTO.UserRecruitProfileResponse;
import com.vn.beta_testing.feature.company_service.service.UserRecruitProfileService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class UserRecruitProfileController {

    private final UserRecruitProfileService userRecruitProfileService;

    public UserRecruitProfileController(UserRecruitProfileService userRecruitProfileService) {
        this.userRecruitProfileService = userRecruitProfileService;
    }

    // Lấy tất cả profiles
    @GetMapping("/recruit-profiles")
    @ApiMessage("Get all recruit profiles")
    public ResponseEntity<ResultPaginationDTO> getAllProfiles(
            @Filter Specification<UserRecruitProfile> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.userRecruitProfileService.fetchAll(spec, pageable));
    }

    // Lấy profile theo id
    @GetMapping("/recruit-profile/{id}")
    @ApiMessage("Get recruit profile by id")
    public ResponseEntity<UserRecruitProfile> getById(@PathVariable("id") Long id) {
        UserRecruitProfile profile = this.userRecruitProfileService.fetchById(id);
        if (profile == null) {
            throw new IdInvalidException("UserRecruitProfile with id = " + id + " does not exist.");
        }
        return ResponseEntity.ok(profile);
    }

    // Lấy profile theo campaign
    @GetMapping("/campaign/{campaignId}/recruit-profile")
    @ApiMessage("Get recruit profile by campaign id")
    public ResponseEntity<UserRecruitProfileResponse> getByCampaign(@PathVariable("campaignId") Long campaignId) {
        UserRecruitProfile profile = this.userRecruitProfileService.fetchByCampaignId(campaignId);
        if (profile == null) {
            throw new IdInvalidException("RecruitProfile for campaign id = " + campaignId + " does not exist.");
        }
        UserRecruitProfileResponse dto = new UserRecruitProfileResponse();
        BeanUtils.copyProperties(profile, dto);
        dto.setCampaignId(profile.getCampaign() != null ? profile.getCampaign().getId() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // Tạo mới
    @PostMapping("/recruit-profile/create")
    @ApiMessage("Create new recruit profile")
    public ResponseEntity<UserRecruitProfileResponse> create(@RequestBody UserRecruitProfile profile) {
        UserRecruitProfile created = userRecruitProfileService.create(profile);
        UserRecruitProfileResponse dto = new UserRecruitProfileResponse();

        BeanUtils.copyProperties(created, dto);
        dto.setCampaignId(created.getCampaign() != null ? created.getCampaign().getId() : null);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // Cập nhật
    @PutMapping("/recruit-profile/update/{id}")
    @ApiMessage("Update recruit profile")
    public ResponseEntity<UserRecruitProfile> update(@PathVariable("id") Long id,
            @RequestBody UserRecruitProfile profile) {
        profile.setId(id);
        UserRecruitProfile updated = this.userRecruitProfileService.update(profile);
        return ResponseEntity.ok(updated);
    }

    // Xóa
    @DeleteMapping("/recruit-profile/delete/{id}")
    @ApiMessage("Delete recruit profile")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.userRecruitProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
