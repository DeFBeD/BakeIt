package com.example.android.bakeit.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrieveBakedTreats {

    public static final String SOURCE_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    static BakedTreatsService sBakedTreatsService;

    public static BakedTreatsService Retrieve() {

        sBakedTreatsService = new Retrofit.Builder()
                .baseUrl(SOURCE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(BakedTreatsService.class);

        return sBakedTreatsService;

    }
}
