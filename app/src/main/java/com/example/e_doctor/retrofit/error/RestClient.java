package com.example.e_doctor.retrofit.error;


import com.example.e_doctor.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Created by PC-JANINA on 8/4/2018.
 */

public class RestClient {
    private static String API_BASE_URL = "http://test.api.buscabienes.net/";
    // private static String API_BASE_URL = "http://192.168.1.175";
    private static String API_HOST_NAME = "dev.api.buscabienes.net";
    private static String APP_KEY = BuildConfig.HEADER_APP_KEY;

    private static Retrofit _retrofit;

    static {
        // enable logging
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        // .header("Host", API_HOST_NAME)
                        .header("APPKey", APP_KEY)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };

        HttpLoggingInterceptor logininterceptor = new HttpLoggingInterceptor();
        logininterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(logininterceptor)
                .build();

        _retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static <S> S get(Class<S> serviceClass) {
        return _retrofit.create(serviceClass);
    }

    public static Retrofit get_retrofit() {
        return _retrofit;
    }
}