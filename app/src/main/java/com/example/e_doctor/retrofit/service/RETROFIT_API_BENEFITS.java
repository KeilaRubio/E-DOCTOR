package com.example.e_doctor.retrofit.service;


import com.example.e_doctor.BuildConfig;
import com.example.e_doctor.core.AppController;
import com.example.e_doctor.core.Config;
import com.example.e_doctor.core.Preferencias;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by app on 6/4/2018.
 */

public class RETROFIT_API_BENEFITS {
    private static RETROFIT_API_BENEFITS instancia;
    private static  APIService service;

    private RETROFIT_API_BENEFITS(){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);
        clientBuilder.connectTimeout(Config.CONNECT_TIMEOUT_RETROFIT, TimeUnit.SECONDS);
        clientBuilder.readTimeout(Config.READ_TIMEOUT_RETROFIT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL_REST_SERVER)//url del api
//                .baseUrl("http://api_nimp.develop.geaecuador.ec/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();
        service = retrofit.create(APIService.class);
    }

    public static APIService settingService() {
        if(instancia==null){
            instancia = new RETROFIT_API_BENEFITS();
        }
        return service;
    }

    //Setea las cabeceras para el API
    public static Map<String, String> getHeaders(){
        Preferencias preferencias = new Preferencias(AppController.getInstance());
        Map<String, String> map = new HashMap<>();
        //map.put("AppKey", BuildConfig.HEADER_APP_KEY); //Api Key
        map.put("Authorization","Bearer"+" "+preferencias.getAccessToken()); //Token de autorización
//        map.put("Device", Build.MANUFACTURER + "-"+ Build.MODEL+"-"+ UtilidadesHelper.getDeviceId(AppController.getInstance().getApplicationContext())); //Versión de la app
        map.put("Client-ID",BuildConfig.CLIENT_ID); //Token de autorización
        map.put("AppVersion",BuildConfig.VERSION_NAME); //Versión de la app
        map.put("Accept", "application/json"); //Api Key
        return map;
    }

}