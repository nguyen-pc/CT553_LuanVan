package com.vn.beta_testing.feature.email_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.beta_testing.feature.email_service.DTO.AccountEmailRequest;
import com.vn.beta_testing.feature.email_service.service.EmailService;
import com.vn.beta_testing.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-account-info")
    @ApiMessage("Send account information email successfully")
    public ResponseEntity<String> sendAccountInfoEmail(
            @RequestBody AccountEmailRequest request) {

        emailService.sendAccountInfoEmail(
                request.getTo(),
                request.getUsername(),
                request.getPassword());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Email sent successfully to " + request.getTo());
    }
}