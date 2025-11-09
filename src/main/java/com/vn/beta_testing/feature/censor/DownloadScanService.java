package com.vn.beta_testing.feature.censor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Service
public class DownloadScanService {

    @Value("${virustotal.api.key}")
    private String vtApiKey;

    private static final String DOWNLOAD_DIR = "C:/Users/NGUYEN_PC/Downloads";

    public Path downloadFile(String fileUrl) throws IOException {
        Files.createDirectories(Paths.get(DOWNLOAD_DIR)); // đảm bảo thư mục tồn tại
        String fileName = "download-" + System.currentTimeMillis();

        // lấy tên file gốc nếu có trong URL
        String urlFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        if (!urlFileName.isEmpty() && urlFileName.length() < 80) {
            fileName = urlFileName;
        }

        Path filePath = Paths.get(DOWNLOAD_DIR, fileName);

        HttpURLConnection conn = null;
        try {
            URL url = new URL(fileUrl);
            conn = (HttpURLConnection) url.openConnection();

            // giả lập trình duyệt thật để tránh bị chặn
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            conn.setRequestProperty("Accept", "*/*");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(20000);

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to download file. HTTP " + responseCode + " from " + fileUrl);
            }

            try (InputStream in = conn.getInputStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("✅ File downloaded to: " + filePath);
            return filePath;

        } catch (Exception e) {
            throw new IOException("Download failed for URL: " + fileUrl + " → " + e.getMessage(), e);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    /**
     * 🧩 Tính hash SHA256 cho file
     */
    public String calculateSHA256(Path filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream is = Files.newInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * 🧩 Kiểm tra hash trên VirusTotal
     */
    public Map<String, Object> checkVirusTotal(String sha256) {
        Map<String, Object> result = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://www.virustotal.com/api/v3/files/" + sha256;

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-apikey", vtApiKey);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                Map<String, Object> attributes = (Map<String, Object>) data.get("attributes");
                Map<String, Object> stats = (Map<String, Object>) attributes.get("last_analysis_stats");

                result.put("found", true);
                result.put("stats", stats);
            } else {
                result.put("found", false);
                result.put("error", "Empty response from VirusTotal");
            }
        } catch (Exception e) {
            result.put("found", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 🧩 Hàm tổng hợp quét link tải
     */
    public Map<String, Object> scanDownload(String url) {
        Map<String, Object> report = new HashMap<>();
        try {
            // Bước 1: Tải file vào thư mục Downloads
            Path filePath = downloadFile(url);

            // Bước 2: Tính SHA256
            String sha256 = calculateSHA256(filePath);

            // Bước 3: Gọi VirusTotal
            Map<String, Object> vtResult = checkVirusTotal(sha256);

            report.put("url", url);
            report.put("filePath", filePath.toString());
            report.put("sha256", sha256);
            report.put("virusTotal", vtResult);
            report.put("status", "DONE");
        } catch (Exception e) {
            e.printStackTrace();
            report.put("status", "ERROR");
            report.put("error", e.getMessage());
        }
        return report;
    }
}
