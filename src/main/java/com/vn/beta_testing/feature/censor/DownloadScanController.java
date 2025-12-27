package com.vn.beta_testing.feature.censor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/censor")
@RequiredArgsConstructor
public class DownloadScanController {

    private final DownloadScanService downloadScanService;

    /**
     * ✅ Endpoint: Quét file từ link tải
     * POST /api/v1/censor/scan-download?url=https://example.com/file.apk
     */
    @PostMapping("/scan-download")
    public ResponseEntity<?> scanDownload(@RequestParam(value = "url", required = false) String url) {
        if (url == null || url.isBlank()) {
            System.out.println("Missing URL parameter for scanDownload");
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "ERROR", "message", "URL parameter is required"));
        }

        Map<String, Object> result = downloadScanService.scanDownload(url);
        return ResponseEntity.ok(result);
    }
}
