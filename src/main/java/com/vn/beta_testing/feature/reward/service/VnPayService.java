package com.vn.beta_testing.feature.reward.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;
import com.vn.beta_testing.config.VnPayConfig;
import com.vn.beta_testing.domain.RewardBatch;

@Service
public class VnPayService {

    private final VnPayConfig config;

    public VnPayService(VnPayConfig config) {
        this.config = config;
    }

    public String createPaymentUrlForBatch(RewardBatch batch, String clientIp)
            throws UnsupportedEncodingException {

        Map<String, String> p = new HashMap<>();
        p.put("vnp_Version", "2.1.0");
        p.put("vnp_Command", "pay");
        p.put("vnp_TmnCode", config.getTmnCode());
        p.put("vnp_Amount", String.valueOf(Math.round(batch.getTotalAmount() * 100)));
        p.put("vnp_CurrCode", "VND");
        p.put("vnp_TxnRef", batch.getBatchCode());
        p.put("vnp_OrderInfo", "ThanhtoanphattruongBatch " + batch.getBatchCode());
        p.put("vnp_OrderType", "other");
        p.put("vnp_BankCode", "NCB");
        p.put("vnp_Locale", "vn");
        p.put("vnp_ReturnUrl", config.getReturnUrl());
        p.put("vnp_IpAddr", (clientIp != null && clientIp.contains(".")) ? clientIp : "127.0.0.1");

        // Tạo thời gian tạo & hết hạn
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        p.put("vnp_CreateDate", fmt.format(cal.getTime()));
        cal.add(Calendar.MINUTE, 15);
        p.put("vnp_ExpireDate", fmt.format(cal.getTime()));

        // sort key alphabetically
        List<String> keys = new ArrayList<>(p.keySet());
        Collections.sort(keys);

        // ✅ Build hashData (bỏ ReturnUrl nếu test localhost)
        List<String> hashKeys = new ArrayList<>();
        for (String k : keys) {
            if (k.equals("vnp_ReturnUrl") && config.getReturnUrl().contains("localhost")) {
                continue;
            }
            hashKeys.add(k);
        }

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < hashKeys.size(); i++) {
            String k = hashKeys.get(i);
            String v = p.get(k);
            if (v != null && !v.isEmpty()) {
                hashData.append(k).append('=').append(v);
                if (i < hashKeys.size() - 1)
                    hashData.append('&');
            }
        }

        // build query string (vẫn giữ ReturnUrl)
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String k = keys.get(i);
            String v = p.get(k);
            if (v != null && !v.isEmpty()) {
                query.append(URLEncoder.encode(k, StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(v, StandardCharsets.UTF_8));
                if (i < keys.size() - 1)
                    query.append('&');
            }
        }

        // ký SHA512
        String secureHash = hmacSHA512(config.getHashSecret().trim(), hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        String url = config.getPayUrl() + "?" + query;
        System.out.println("🔍 HashData = " + hashData);
        System.out.println("🔍 Secret.len = " + config.getHashSecret().trim().length());
        System.out.println("🔍 URL = " + url);
        return url;
    }

    // ✅ Xác minh callback checksum
    public boolean verifyChecksum(Map<String, String> params) {
        String provided = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        // ✅ Loại bỏ ReturnUrl nếu test localhost
        List<String> hashKeys = new ArrayList<>();
        for (String k : keys) {
            if (k.equals("vnp_ReturnUrl") && config.getReturnUrl().contains("localhost")) {
                continue;
            }
            hashKeys.add(k);
        }

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < hashKeys.size(); i++) {
            String k = hashKeys.get(i);
            String v = params.get(k);
            if (v != null && !v.isEmpty()) {
                hashData.append(k).append('=').append(v);
                if (i < hashKeys.size() - 1)
                    hashData.append('&');
            }
        }

        String recalculated = hmacSHA512(config.getHashSecret().trim(), hashData.toString());
        System.out.println("🔎 VERIFY hashData=" + hashData);
        System.out.println("🔎 VERIFY calc   =" + recalculated);
        System.out.println("🔎 VERIFY given  =" + provided);
        return recalculated.equalsIgnoreCase(provided);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] out = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : out)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("❌ Lỗi khi ký HMAC SHA512", e);
        }
    }
}
