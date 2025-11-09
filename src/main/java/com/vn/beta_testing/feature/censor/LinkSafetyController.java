package com.vn.beta_testing.feature.censor;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/censor")
@RequiredArgsConstructor
public class LinkSafetyController {

    private final LinkSafetyService linkSafetyService;

    @GetMapping("/check-link")
    public ResponseEntity<?> checkLinkSafety(@RequestParam("url") String url) {
        try {
            if (url == null || url.isBlank()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "status", "ERROR",
                                "message", "URL parameter is required"
                        ));
            }

            boolean safe = linkSafetyService.isUrlSafe(url);
            if (safe) {
                return ResponseEntity.ok(Map.of(
                        "status", "SAFE",
                        "message", "URL is safe",
                        "url", url
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "UNSAFE",
                    "message", "⚠️ URL may be unsafe or malicious",
                    "url", url
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "ERROR",
                            "message", "Failed to check URL safety",
                            "error", e.getMessage()
                    ));
        }
    }
}