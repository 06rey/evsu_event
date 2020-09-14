package com.evsu.event.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.evsu.event.App;
import com.evsu.event.R;
import com.evsu.event.ui.auth.LoginActivity;
import com.evsu.event.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent;

        if (App.instance().hasActiveUser()) {
            intent = new Intent(SplashActivity.this, HomeActivity.class);
        }else{
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(runnable, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
