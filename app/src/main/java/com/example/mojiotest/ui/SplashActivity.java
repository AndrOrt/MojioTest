package com.example.mojiotest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mojiotest.R;
import com.example.mojiotest.tools.PrefManager;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    public PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(getApplicationContext());

        boolean isLogin = prefManager.isLogin();
        if (isLogin) {
            startMainActivity();
        } else {
            //TODO start LoginActivity
            // startActivity(new Intent(this, LoginActivity.class));

            final MojioClient mojioClient = ((App) getApplicationContext()).getMojioClient();
            mojioClient.login("andr-ort@mail.ru", "EpamMojio").enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    startMainActivity();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_SHORT).show();
                    finishWithAnimation();
                }
            });
        }
    }

    private void finishWithAnimation() {
        finish();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
