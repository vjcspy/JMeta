/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.service;


import com.vjcspy.spring.packages.stocksync.client.vietstock.VietStockCredential;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VietStockCredentialService {
    @Autowired
    private RestTemplate restTemplate;

    private VietStockCredential cachedCredentials;

    public VietStockCredential retrieveCredentials() {
        if (cachedCredentials == null) {
            String loggedCrds = loggedCookies();
            String sid = "1i1225usk5lljrcwny0tmffl";
            String rvt =
                    "nLE53UKUb9eZX-5Nv3aMQ4jYuOZ-2Y9nvkGXZ0dAM1TYu_7tHQPsDhyrKF87cZu423xFKHggL0kq-ywWhRMEe8ZKpoH7Lc8X2QDQ0YSrfZM1";
            String vtsUsrLg =
                    "3C423246818F0E3528187CCAAC8884C7DC2AC16F9370B0F46310C7C97868240303DA0954B5ABFD2F42DBFAD6F7E849A820ED218753FBE20CFE166B2C0CF57CB3781FC08C58FD42AB62243B08DB47839FD9C85C2C1492899C1E2EAA852FB5384C5967E0EA4C74D4E0A3D959F9FED70743D68F7E7139D3C4B3B60DC6D726AE60D2";
            String usrTk = "wAoBVyA7RUuS5D/peNITBQ==";

            Pattern pattern = Pattern.compile(
                    "(.*)(ASP.NET_SessionId=.*;)(.*)(__RequestVerificationToken=.*;)(.*)(vts_usr_lg=.*;)(.*)(vst_usr_lg_token=.*;)(.*)");
            Matcher matcher = pattern.matcher(loggedCrds);
            if (matcher.find()) {
                sid = extractValue(matcher.group(2));
                rvt = extractValue(matcher.group(4));
                vtsUsrLg = extractValue(matcher.group(6));
                usrTk = extractValue(matcher.group(8));
            }

            Map<String, String> csrfAfterLogin = retrieveCookiesAndCsrf(loggedCrds, false);
            cachedCredentials = VietStockCredential.builder()
                    .sid(sid)
                    .rvt(rvt)
                    .vtsUsrLg(vtsUsrLg)
                    .usrTk(usrTk)
                    .csrf(csrfAfterLogin.get("csrf"))
                    .build();
        }

        return cachedCredentials;
    }

    private String loggedCookies() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var data = retrieveCookiesAndCsrf(null, true);
        headers.add("cookie", data.get("cookies"));

        HttpEntity<String> request = new HttpEntity<>(
                "__RequestVerificationToken=" + data.get("csrf")
                        + "&Email=dinhkhoi.le05%40gmail.com&Password=536723&responseCaptchaLoginPopup=&g-recaptcha-response=&Remember=false&X-Requested-With=XMLHttpRequest",
                headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://finance.vietstock.vn/Account/Login", HttpMethod.POST, request, String.class);
        return Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).toString();
    }

    private Map<String, String> retrieveCookiesAndCsrf(String initCookies, boolean needCookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                "accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.add("accept-language", "vi,en-US;q=0.9,en;q=0.8");
        headers.add("cache-control", "no-cache");
        headers.add("pragma", "no-cache");
        headers.add("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"");
        headers.add("sec-ch-ua-mobile", "?0");
        headers.add("sec-ch-ua-platform", "\"macOS\"");
        headers.add("sec-fetch-dest", "document");
        headers.add("sec-fetch-mode", "navigate");
        headers.add("sec-fetch-site", "same-origin");
        headers.add("sec-fetch-user", "?1");
        headers.add("upgrade-insecure-requests", "1");
        headers.add("Referer", "https://www.google.com/");
        headers.add("Referrer-Policy", "origin");

        if (initCookies != null) {
            headers.add("cookie", initCookies);
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1",
                HttpMethod.GET,
                entity,
                String.class);

        // Lấy CSRF token từ HTML
        Document doc = Jsoup.parse(response.getBody());
        Element csrfInput = doc.select("[name=__RequestVerificationToken]").first();
        String csrf = csrfInput != null ? csrfInput.attr("value") : "";

        // Lấy cookies nếu cần
        String cookies = needCookie ? parseCookies(response.getHeaders()) : "";
        Map<String, String> data = new HashMap<>();
        data.put("csrf", csrf);
        data.put("cookies", cookies);

        return data;
    }

    private String extractValue(String cookie) {
        return cookie.substring(cookie.indexOf('=') + 1, cookie.length() - 1);
    }

    private String parseCookies(HttpHeaders headers) {
        List<String> rawCookies = headers.get("Set-Cookie");
        if (rawCookies == null) return "";

        return rawCookies.stream()
                .map(cookie -> cookie.split(";", 2)[0]) // Lấy phần cookie trước dấu ";"
                .collect(Collectors.joining("; "));
    }
}
