package com.example.solusyoninternetserviceprovider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ShapeableImageView ivProfile;
    private TextView tvSolusyonLogo;
    private DatabaseReference mDatabase;
    private final String DB_URL = "https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private boolean isUserLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Initial State Check
        boolean isSubscriber = getIntent().getBooleanExtra("IS_SUBSCRIBER", false);
        boolean isAutoLogin = getIntent().getBooleanExtra("IS_AUTO_LOGIN", false);
        boolean shouldShowLogin = getIntent().getBooleanExtra("SHOW_LOGIN", false);

        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();

        if (savedInstanceState == null) {
            if (shouldShowLogin) {
                setLayout(false);
                loadFragment(new LoginFragment(), false);
                toggleSystemUI(false);
            } else if (isSubscriber) {
                setLayout(true);
                loadFragment(new UserDashboardFragment(), false);
            } else if (isAutoLogin || FirebaseAuth.getInstance().getCurrentUser() != null) {
                performSessionRoleCheck();
            } else {
                setLayout(false);
                loadFragment(new DashboardFragment(), false);
                toggleSystemUI(true);
            }
        }
    }

    /**
     * Switches between Admin layout (activity_main) and Subscriber layout (activity_main_user)
     */
    private void setLayout(boolean userLayout) {
        this.isUserLayout = userLayout;
        if (userLayout) {
            setContentView(R.layout.activity_main_user);
            setupUserNavigation();
        } else {
            setContentView(R.layout.activity_main);
            setupAdminNavigation();
        }
        syncWelcomeHeader();
        ivProfile = findViewById(R.id.ivProfile);
        if (ivProfile != null) {
            ivProfile.setOnClickListener(v -> showLogoutDialog());
        }
    }

    /**
     * Set up the BottomNavigationView for the Admin Layout
     */
    private void setupAdminNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_dashboard) {
                    selectedFragment = new DashboardFragment();
                } else if (itemId == R.id.nav_subscribers) {
                    selectedFragment = new SubscriberManagement();
                } else if (itemId == R.id.nav_billing) {
                    selectedFragment = new BillingFragment();
                } else if (itemId == R.id.nav_reports) {
                    selectedFragment = new ReportsFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment, true);
                }
                return true;
            });
        }
    }

    /**
     * Set up the custom navigation for the User Layout
     */
    private void setupUserNavigation() {
        View navDashboard = findViewById(R.id.nav_dashboard);
        View navBilling = findViewById(R.id.nav_billing);
        View navProfile = findViewById(R.id.nav_profile);

        if (navDashboard != null) {
            navDashboard.setOnClickListener(v -> loadFragment(new UserDashboardFragment(), true));
        }
        if (navBilling != null) {
            navBilling.setOnClickListener(v -> loadFragment(new UserBillingFragment(), true));
        }
        if (navProfile != null) {
            navProfile.setOnClickListener(v -> {
                // Navigate to SubscriberProfileFragment
                loadFragment(new SubscriberProfileFragment(), true);
            });
        }
    }
    /**
     * Updates the header text based on the saved user session.
     */
    public void syncWelcomeHeader() {
        tvSolusyonLogo = findViewById(R.id.tvSolusyonLogo);
        if (tvSolusyonLogo != null) {
            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            String fullName = prefs.getString("userName", "");

            if (!fullName.isEmpty()) {
                // Split to only show the first name
                String firstName = fullName.split(" ")[0];
                tvSolusyonLogo.setText("Welcome, " + firstName + "!");
            } else {
                tvSolusyonLogo.setText("SOLUSYON");
            }
        }
    }

    private void performSessionRoleCheck() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("fullName").getValue(String.class);
                    if (name == null || name.isEmpty()) {
                        name = snapshot.child("username").getValue(String.class);
                    }

                    if (name != null) {
                        getSharedPreferences("UserSession", MODE_PRIVATE)
                                .edit().putString("userName", name).apply();
                    }

                    String role = snapshot.child("role").getValue(String.class);
                    if ("subscriber".equalsIgnoreCase(role)) {
                        setLayout(true); // Switch to User Layout
                        checkSubscriberStatus(uid);
                    } else {
                        setLayout(false); // Switch to Admin Layout
                        loadFragment(new DashboardFragment(), false);
                        toggleSystemUI(true);
                    }
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void checkSubscriberStatus(String uid) {
        mDatabase.child("ServiceApplications").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.child("status").getValue(String.class);

                if ("completed".equalsIgnoreCase(status) || "approved".equalsIgnoreCase(status)) {
                    loadFragment(new UserBillingFragment(), false);
                } else if (snapshot.exists()) {
                    Intent intent = new Intent(MainActivity.this, UserApplicationReceiptActivity.class);
                    intent.putExtra("appId", snapshot.child("applicationId").getValue(String.class));
                    intent.putExtra("fullName", snapshot.child("fullName").getValue(String.class));
                    intent.putExtra("phone", snapshot.child("phone").getValue(String.class));
                    intent.putExtra("plan", snapshot.child("plan").getValue(String.class));
                    intent.putExtra("payment", snapshot.child("payment").getValue(String.class));
                    intent.putExtra("date", snapshot.child("date").getValue(String.class));
                    startActivity(intent);
                    finish();
                } else {
                    loadFragment(new UserDashboardFragment(), false);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Account")
                .setMessage("Would you like to log out of your session?")
                .setPositiveButton("Logout", (dialog, which) -> {
                    getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void toggleSystemUI(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        if (bottomNavigationView != null) bottomNavigationView.setVisibility(visibility);
        if (ivProfile != null) ivProfile.setVisibility(visibility);
    }

    private void loadFragment(Fragment fragment, boolean animate) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (animate) {
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}