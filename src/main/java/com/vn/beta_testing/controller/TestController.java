package com.vn.beta_testing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test successful: ");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test1() {
        return ResponseEntity.ok("Test successful: ");
    }
}
