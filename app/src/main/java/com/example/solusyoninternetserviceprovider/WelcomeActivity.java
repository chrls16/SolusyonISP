package com.example.solusyoninternetserviceprovider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 1. Hide the Action Bar for a full-screen look
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 2. Initialize the views from activity_welcome.xml
        Button btnApply = findViewById(R.id.btnApply);
        TextView btnAdmin = findViewById(R.id.btnAdmin);

        // 3. Logic for "Apply for Fiber Internet" button
        btnApply.setOnClickListener(v -> {
            // This will open the User Account Creation interface
            Intent intent = new Intent(WelcomeActivity.this, UserCreateAccountActivity.class);
            startActivity(intent);
        });

        // 4. Logic for "ADMIN/STAFF LOGIN" text button
        btnAdmin.setOnClickListener(v -> {
            // We go to MainActivity and pass a flag to show the LoginFragment
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra("SHOW_LOGIN", true);
            startActivity(intent);
        });
    }
}
