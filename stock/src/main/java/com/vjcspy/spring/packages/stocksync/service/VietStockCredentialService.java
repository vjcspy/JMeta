/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.service;


import com.vjcspy.spring.packages.stocksync.dto.vietstock.VietStockCredential;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class VietStockCredentialService {

    private VietStockCredential cachedCredentials;
    private OkHttpClient client = new OkHttpClient();

    public VietStockCredentialService(RestTemplate restTemplate) {
    }

    public VietStockCredential retrieveCredentials() {
        if (cachedCredentials == null) {
            var loggedCrds = loggedCookies();
            String sid = "";
            String rvt = "";
            String vtsUsrLg = "";
            String usrTk = "";

            Pattern pattern = Pattern.compile(
                    "(.*)(ASP.NET_SessionId=.*;)(.*)(__RequestVerificationToken=.*;)(.*)(vts_usr_lg=.*;)(.*)(vst_usr_lg_token=.*;)(.*)");
            Matcher matcher = pattern.matcher(loggedCrds.get("cookies"));
            if (matcher.find()) {
                sid = extractValue(matcher.group(2));
                rvt = extractValue(matcher.group(4));
                vtsUsrLg = extractValue(matcher.group(6));
                usrTk = extractValue(matcher.group(8));
            }

            Map<String, String> csrfAfterLogin = retrieveCookiesAndCsrf(loggedCrds.get("cookies"), false);
            assert csrfAfterLogin != null;
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

    private Map<String, String> loggedCookies() {
        try {
            Map<String, String> initialData = retrieveCookiesAndCsrf(null, true);
            String cookies = Objects.requireNonNull(initialData).get("cookies");
            String csrf = initialData.get("csrf");

            String url = "https://finance.vietstock.vn/Account/Login";
            String requestBody = String.format(
                    "__RequestVerificationToken=%s&Email=%s&Password=%s&responseCaptchaLoginPopup=&g-recaptcha-response=&Remember=false&X-Requested-With=XMLHttpRequest",
                    csrf,
                    "dinhkhoi.le05@gmail.com",
                    "536723"
            );

            RequestBody body = RequestBody.create(
                    requestBody,
                    okhttp3.MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("accept", "*/*")
                    .addHeader("accept-language", "vi,en-US;q=0.9,en;q=0.8")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .addHeader("pragma", "no-cache")
                    .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"macOS\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("cookie", cookies)
                    .addHeader("Referer", "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1")
                    .addHeader("Referrer-Policy", "strict-origin-when-cross-origin")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.info("Login error, status: {}", response.code());
                    return new HashMap<>();
                }

                // In nội dung response sau khi login
                assert response.body() != null;
                log.info("login vietstock res: {}", response.body().string());

                // Lấy cookies từ response sau khi đăng nhập
                String cookiesAfterLogin = parseCookies(response.headers("Set-Cookie"));

                Map<String, String> result = new HashMap<>();
                result.put("cookies", cookies + "; " + cookiesAfterLogin);

                return result;
            }
        } catch (IOException e) {
            log.error("VietStock Login error");
        }

        return new HashMap<>();
    }

    private Map<String, String> retrieveCookiesAndCsrf(String initCookies, boolean needCookie) {
        String url = "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1";

        // Tạo request với các header giống như trong HttpHeaders
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("accept-language", "vi,en-US;q=0.9,en;q=0.8")
                .addHeader("cache-control", "no-cache")
                .addHeader("pragma", "no-cache")
                .addHeader("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("Referer", "https://www.google.com/")
                .addHeader("Referrer-Policy", "origin");

        // Thêm cookie vào header nếu có
        if (initCookies != null) {
            requestBuilder.addHeader("cookie", initCookies);
        }

        // Tạo request
        Request request = requestBuilder.build();

        // Thực hiện request và xử lý response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse HTML để lấy CSRF token từ body response
            assert response.body() != null;
            String responseBody = response.body().string();
            Document doc = Jsoup.parse(responseBody);
            Element csrfInput = doc.select("[name=__RequestVerificationToken]").first();
            String csrf = csrfInput != null ? csrfInput.attr("value") : "";

            // Lấy cookies từ header response nếu cần
            String cookies = "";
            if (needCookie) {
                cookies = parseCookies(response.headers("Set-Cookie"));
            }

            // Trả về dữ liệu dưới dạng Map
            Map<String, String> data = new HashMap<>();
            data.put("csrf", csrf);
            data.put("cookies", cookies);

            return data;
        } catch (IOException e) {
            log.error("retrieveCookiesAndCsrf error", e);
        }

        return null;
    }

    private String extractValue(String cookie) {
        return cookie.substring(cookie.indexOf('=') + 1, cookie.length() - 1);
    }

    private String parseCookies(List<String> rawCookies) {
        if (rawCookies == null) return "";

        return rawCookies.stream()
                .map(cookie -> cookie.split(";", 2)[0]) // Lấy phần cookie trước dấu ";"
                .collect(Collectors.joining("; "));
    }
}
