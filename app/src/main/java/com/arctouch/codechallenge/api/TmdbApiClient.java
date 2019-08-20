package com.arctouch.codechallenge.api;

import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class TmdbApiClient {

    private static TmdbApi API;
    private static String URL = "https://api.themoviedb.org/3/";

    public static String API_KEY = "1f54bd990f1cdfb230adb312546d765d";
    public static String DEFAULT_LANGUAGE = Locale.getDefault().toString().replace("_", "-");
    public static String DEFAULT_REGION = Locale.getDefault().getCountry();


    public static TmdbApi getApi() {
        if (API == null) {
            API = new Retrofit.Builder()
                    .baseUrl(TmdbApiClient.URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(TmdbApi.class);
        }

        return API;
    }
}
