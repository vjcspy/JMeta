/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CorService {
    private final OkHttpClient client = new OkHttpClient();
    private final VietStockCredentialService vietStockCredentialService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CorService(VietStockCredentialService vietStockCredentialService) {
        this.vietStockCredentialService = vietStockCredentialService;
    }

    public List<Map<String, Object>> getCorporateData(int page) {
        var vietStockCredential = vietStockCredentialService.retrieveCredentials();
        String sid = vietStockCredential.getSid();
        String rvt = vietStockCredential.getRvt();
        String vtsUsrLg = vietStockCredential.getVtsUsrLg();
        String usrTk = vietStockCredential.getUsrTk();
        String csrf = vietStockCredential.getCsrf();

        //        var vietStockCredentialData = getCredentialFromNode();
        //        String sid = vietStockCredentialData.get("sid");
        //        String rvt = vietStockCredentialData.get("rvt");
        //        String vtsUsrLg = vietStockCredentialData.get("vtsUsrLg");
        //        String usrTk = vietStockCredentialData.get("usrTk");
        //        String csrf = vietStockCredentialData.get("csrf");

        // Tạo request body
        String requestBody = String.format(
                "catID=0&industryID=0&page=%d&pageSize=10&type=1&code=&businessTypeID=0&orderBy=Code&orderDir=ASC&__RequestVerificationToken=%s",
                page, csrf);

        // Tạo request
        Request request = new Request.Builder()
                .url("https://finance.vietstock.vn/data/corporateaz")
                .post(RequestBody.create(
                        requestBody, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8")))
                .addHeader(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
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
                .addHeader(
                        "cookie",
                        String.format(
                                "ASP.NET_SessionId=%s; __RequestVerificationToken=%s; language=vi-VN; Theme=Light; isShowLogin=true; vts_usr_lg=%s; vst_usr_lg_token=%s",
                                sid, rvt, vtsUsrLg, usrTk))
                .addHeader("Referer", "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1")
                .addHeader("Referrer-Policy", "strict-origin-when-cross-origin")
                .build();

        // Gửi request và xử lý phản hồi
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                List<Map<String, Object>> corporateData = objectMapper.readValue(
                        response.body().string(), new TypeReference<List<Map<String, Object>>>() {});
                return corporateData;
            }
        } catch (Exception ex) {
            log.error("Error getting Corporate data", ex);
        }

        return null;
    }

    /*
        private Map<String, String> getCredentialFromNode() {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url("http://localhost:3000/cor/test")
                    .method("GET", null)
                    .build();
            LinkedHashMap<String, String> vietStockCredentialData = new LinkedHashMap<>();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    // Đọc body của response dưới dạng String
                    String responseBody = response.body().string();

                    // Parse JSON string sang Map hoặc Object bằng Jackson
                    vietStockCredentialData = JsonPath.read(responseBody, "$.vsCreds");

                    // In ra để kiểm tra kết quả
                    System.out.println("Response JSON as Map: " + vietStockCredentialData);
                } else {
                    System.out.println("Request failed: " + response.code());
                }
            } catch (IOException ignored) {
            }

            return vietStockCredentialData;
        }

        public String retrieveCor(int page) {
            var vietStockCredentialData = getCredentialFromNode();
            String sid = vietStockCredentialData.get("sid");
            String rvt = vietStockCredentialData.get("rvt");
            String vtsUsrLg = vietStockCredentialData.get("vtsUsrLg");
            String usrTk = vietStockCredentialData.get("usrTk");
            String csrf = vietStockCredentialData.get("csrf");

            String url = "https://finance.vietstock.vn/data/corporateaz";

            String bodyContent = String.format(
                    "catID=0&industryID=0&page=%d&pageSize=50&type=1&code=&businessTypeID=0&orderBy=Code&orderDir=ASC&__RequestVerificationToken=%s",
                    page, csrf);

            RequestBody body =
                    RequestBody.create(bodyContent, MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"));

            Request requestGetCor = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
                    .addHeader("accept", "* /*")
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
                    .addHeader(
                            "cookie",
                            String.format(
                                    "ASP.NET_SessionId=%s; __RequestVerificationToken=%s; language=vi-VN; Theme=Light; isShowLogin=true; vts_usr_lg=%s; vst_usr_lg_token=%s",
                                    sid, rvt, vtsUsrLg, usrTk))
                    .addHeader("Referer", "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1")
                    .addHeader("Referrer-Policy", "strict-origin-when-cross-origin")
                    .build();

            try (Response response = client.newCall(requestGetCor).execute()) {
                if (!response.isSuccessful()) {
                    return null;
                }
                assert response.body() != null;
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    */
}
