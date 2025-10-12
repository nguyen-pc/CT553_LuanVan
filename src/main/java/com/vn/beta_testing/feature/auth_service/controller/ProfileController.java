package com.vn.beta_testing.feature.auth_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.domain.UserProfile;
import com.vn.beta_testing.feature.auth_service.service.ProfileService;
import com.vn.beta_testing.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/user/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    @ApiMessage("Get user profile by user ID")
    public ResponseEntity<UserProfile> getProfileByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @PostMapping("/{userId}")
    @ApiMessage("Create or update user profile")
    public ResponseEntity<UserProfile> saveOrUpdateProfile(
            @PathVariable("userId") Long userId,
            @RequestBody UserProfile profileData) {
        UserProfile saved = profileService.saveOrUpdateProfile(userId, profileData);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}