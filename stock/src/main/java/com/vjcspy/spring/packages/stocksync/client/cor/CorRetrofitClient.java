package com.vjcspy.spring.packages.stocksync.client.cor;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CorRetrofitClient {
    private static final String BASE_URL = "https://finance.vietstock.vn/";

    public static CorApiService createApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(CorApiService.class);
    }
}
