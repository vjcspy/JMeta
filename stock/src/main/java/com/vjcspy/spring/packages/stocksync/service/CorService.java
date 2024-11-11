/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.service;

import com.vjcspy.spring.packages.stocksync.client.cor.CorApiService;
import com.vjcspy.spring.packages.stocksync.client.cor.CorRetrofitClient;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.springframework.stereotype.Service;

@Service
public class CorService {
    private final CorApiService corApiService;

    public CorService() {
        this.corApiService = CorRetrofitClient.createApiService();
    }

    public Single<String> getCorporateData(
            String sid, String rvt, String vtsUsrLg, String usrTk, String csrf, int page) {
        // Tạo giá trị cookie
        String cookie = String.format(
                "ASP.NET_SessionId=%s; __RequestVerificationToken=%s; language=vi-VN; Theme=Light; isShowLogin=true; vts_usr_lg=%s; vst_usr_lg_token=%s",
                sid, rvt, vtsUsrLg, usrTk);
        String referer = "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1";

        // Tạo body cho request
        String body = String.format(
                "catID=0&industryID=0&page=%d&pageSize=50&type=1&code=&businessTypeID=0&orderBy=Code&orderDir=ASC&__RequestVerificationToken=%s",
                page, csrf);

        // Gọi API với RxJava
        return corApiService
                .getCorporateData(cookie, referer, body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation());
    }
}
