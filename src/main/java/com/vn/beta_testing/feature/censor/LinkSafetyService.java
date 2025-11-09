package com.vn.beta_testing.feature.censor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LinkSafetyService {

    @Value("${google.safebrowsing.api-key}")
    private String apiKey;

    private static final String GOOGLE_SAFE_BROWSING_URL = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=";

    /**
     * Kiểm tra URL có an toàn hay không bằng Google Safe Browsing
     * @param url link cần kiểm tra
     * @return true nếu an toàn, false nếu phát hiện nguy hiểm
     */
    public boolean isUrlSafe(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> body = Map.of(
                    "client", Map.of(
                            "clientId", "betatesting-platform",
                            "clientVersion", "1.0"
                    ),
                    "threatInfo", Map.of(
                            "threatTypes", List.of(
                                    "MALWARE",
                                    "SOCIAL_ENGINEERING",
                                    "UNWANTED_SOFTWARE",
                                    "POTENTIALLY_HARMFUL_APPLICATION"
                            ),
                            "platformTypes", List.of("ANY_PLATFORM"),
                            "threatEntryTypes", List.of("URL"),
                            "threatEntries", List.of(Map.of("url", url))
                    )
            );

            String fullUrl = GOOGLE_SAFE_BROWSING_URL + apiKey;
            ResponseEntity<Map> response = restTemplate.postForEntity(fullUrl, body, Map.class);

            // Nếu response body rỗng => an toàn
            return (response.getBody() == null || response.getBody().isEmpty());
        } catch (Exception e) {
            System.err.println("❌ SafeBrowsing check error: " + e.getMessage());
            // Nếu lỗi, coi như không an toàn để tránh bypass
            return false;
        }
    }

    /**
     * Hàm kiểm tra và trả về thông tin chi tiết
     */
    public String checkUrlStatus(String url) {
        boolean safe = isUrlSafe(url);
        return safe ? "SAFE" : "UNSAFE";
    }
}