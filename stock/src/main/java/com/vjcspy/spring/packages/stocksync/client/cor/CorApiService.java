package com.vjcspy.spring.packages.stocksync.client.cor;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CorApiService {
    @Headers({
            "accept: */*",
            "accept-language: vi,en-US;q=0.9,en;q=0.8",
            "cache-control: no-cache",
            "content-type: application/x-www-form-urlencoded; charset=UTF-8",
            "pragma: no-cache",
            "sec-ch-ua: \" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"",
            "sec-ch-ua-mobile: ?0",
            "sec-ch-ua-platform: \"macOS\"",
            "sec-fetch-dest: empty",
            "sec-fetch-mode: cors",
            "sec-fetch-site: same-origin",
            "x-requested-with: XMLHttpRequest",
            "Referrer-Policy: strict-origin-when-cross-origin"
    })
    @POST("data/corporateaz")
    Single<String> getCorporateData(
            @Header("cookie") String cookie,
            @Header("Referer") String referer,
            @Body String body
    );
}
