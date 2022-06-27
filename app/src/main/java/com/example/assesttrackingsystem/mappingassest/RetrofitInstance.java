package com.example.assesttrackingsystem.mappingassest;

import com.example.assesttrackingsystem.ApiInterface;
import com.example.assesttrackingsystem.Global;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static RetrofitInstance instance = null;
    private ApiInterface apiInterface;

    private RetrofitInstance(){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitInstance getInstance() {
        if (instance == null){
            instance = new RetrofitInstance();
        }
        return instance;
    }

    public ApiInterface getApiInterface(){
        return apiInterface;
    }

}

