package com.example.mojiotest;

import android.app.Application;

import com.google.gson.Gson;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.MojioEnvironment;

public class App extends Application {

    private static final String APP_ID = "36679cae-d8cf-42c5-bf7b-eaabe49ce24a";
    private static final String APP_SECRET = "07e0b3c6-c823-49d3-8946-926c681272a4";

    private MojioClient mojioClient;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        gson = new Gson();
        mojioClient = new MojioClient.Builder(APP_ID, APP_SECRET)
                .environment(MojioEnvironment.STAGING)
                .logging(BuildConfig.DEBUG)
                .gson(gson)
                .build();
    }

    public MojioClient getMojioClient() {
        return mojioClient;
    }

    public Gson getGson() {
        return gson;
    }
}
