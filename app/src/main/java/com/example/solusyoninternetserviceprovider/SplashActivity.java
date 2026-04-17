package com.example.solusyoninternetserviceprovider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide the top action bar so the splash screen is full screen
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Delay for 2.5 seconds (2500 milliseconds)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Change MainActivity.class to LoginActivity.class if you have a separate login screen!
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);

            // finish() prevents the user from pressing the back button to return to the loading screen
            finish();

        }, 2500);
    }
}