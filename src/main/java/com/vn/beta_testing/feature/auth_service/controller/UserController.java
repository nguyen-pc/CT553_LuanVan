package com.vn.beta_testing.feature.auth_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.beta_testing.domain.User;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.domain.response.user.ResCreateUserDTO;
import com.vn.beta_testing.domain.response.user.ResUpdateUserDTO;
import com.vn.beta_testing.domain.response.user.ResUserDTO;
import com.vn.beta_testing.feature.auth_service.DTO.ReqChangePasswordDTO;
import com.vn.beta_testing.feature.auth_service.service.UserService;
import com.vn.beta_testing.util.annotation.ApiMessage;
import com.vn.beta_testing.util.error.IdInvalidException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser)
            throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email" + postManUser.getEmail() + "da ton tai, vui long su dung email khac");
        }
        String hasPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hasPassword);
        User user = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User voi id = " + id + " khong ton tai");
        }

        this.userService.handleDeleteUser(id);

        return ResponseEntity.status(HttpStatus.OK).body("delete success");
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {

        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User voi id = " + id + "khong ton tai");
        }

        ResUserDTO dto = this.userService.convertToResUserDTO(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional
    ) {

        // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        // String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() :
        // "";

        // int current = Integer.parseInt(sCurrent);
        // int pageSize = Integer.parseInt(sPageSize);
        // Pageable pageable = PageRequest.of(current - 1, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> UpdateUser(@RequestBody User user) throws IdInvalidException {
        System.out.println("Update user: " + user);
        User curUser = this.userService.handleUpdateUserNotNull(user);

        if (curUser == null) {
            throw new IdInvalidException("user voi id = " + user.getId() + "Khong ton tai");
        }

        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(user));
    }

    @PutMapping("/users/change-password")
    @ApiMessage("Change user password")
    public ResponseEntity<String> changePassword(@RequestBody ReqChangePasswordDTO req) {
        try {
            boolean success = userService.handleChangePassword(req.getId(), req.getOldPassword(), req.getNewPassword());
            if (success) {
                return ResponseEntity.ok("Password updated successfully");
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body("Failed to update password: " + ex.getMessage());
        }
        return ResponseEntity.internalServerError()
                .body("Unexpected error");
    }

    @GetMapping("/users/company/{companyId}")
    @ApiMessage("Fetch users by company ID")
    public ResponseEntity<List<ResUserDTO>> getUsersByCompanyId(@PathVariable("companyId") Long companyId) {
        List<ResUserDTO> users = userService.fetchUsersByCompanyId(companyId);

        if (users == null || users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/latest")
    @ApiMessage("Get top 10 newest users")
    public ResponseEntity<List<ResUserDTO>> getTop10NewestUsers() {
        List<ResUserDTO> newestUsers = userService.fetchTop10NewestUsers();
        return ResponseEntity.ok(newestUsers);
    }
}
