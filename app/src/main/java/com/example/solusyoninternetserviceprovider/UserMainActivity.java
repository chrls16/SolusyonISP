package com.example.solusyoninternetserviceprovider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;

public class UserMainActivity extends AppCompatActivity {

    private LinearLayout navDashboard, navBilling, navProfile;
    private TextView tvSolusyonLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        // 1. Initialize Views
        navDashboard = findViewById(R.id.nav_dashboard);
        navBilling = findViewById(R.id.nav_billing);
        navProfile = findViewById(R.id.nav_profile);
        tvSolusyonLogo = findViewById(R.id.tvSolusyonLogo);

        // 2. Set default fragment based on intent or default to Billing
        String targetFragment = getIntent().getStringExtra("TARGET_FRAGMENT");
        if ("DASHBOARD".equals(targetFragment)) {
            loadFragment(new UserDashboardFragment(), false);
            updateNavUI(navDashboard);
        } else {
            loadFragment(new UserBillingFragment(), false);
            updateNavUI(navBilling);
        }

        // 3. Listeners
        navDashboard.setOnClickListener(v -> {
            loadFragment(new UserDashboardFragment(), true);
            updateNavUI(navDashboard);
        });

        navBilling.setOnClickListener(v -> {
            loadFragment(new UserBillingFragment(), true);
            updateNavUI(navBilling);
        });

        navProfile.setOnClickListener(v -> showLogoutDialog());

        // 4. Initial Header Sync
        syncWelcomeHeader();
    }

    public void syncWelcomeHeader() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String fullName = prefs.getString("userName", "");

        if (!fullName.isEmpty()) {
            String firstName = fullName.split(" ")[0];
            tvSolusyonLogo.setText("Welcome, " + firstName + "!");
        } else {
            tvSolusyonLogo.setText("SOLUSYON");
        }
    }

    private void updateNavUI(View activeItem) {
        // Reset all navigation items to default state
        navDashboard.setBackgroundResource(R.drawable.nav_item_bg);
        navBilling.setBackgroundResource(R.drawable.nav_item_bg);
        navProfile.setBackgroundResource(R.drawable.nav_item_bg);

        // Set active state for the clicked item
        activeItem.setBackgroundResource(R.drawable.bg_nav_active);
    }

    private void loadFragment(Fragment fragment, boolean animate) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Account")
                .setMessage("Would you like to log out of your session?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(UserMainActivity.this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
