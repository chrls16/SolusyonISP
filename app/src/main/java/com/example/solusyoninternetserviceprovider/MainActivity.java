package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private View headerLayout; // To hide the header during login
    private ShapeableImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        ivProfile = findViewById(R.id.ivProfile);

        // CHECK FOR FLAGS
        boolean shouldShowLogin = getIntent().getBooleanExtra("SHOW_LOGIN", false);
        boolean isSubscriber = getIntent().getBooleanExtra("IS_SUBSCRIBER", false);

        if (savedInstanceState == null) {
            if (shouldShowLogin) {
                loadFragment(new LoginFragment(), false);
                toggleSystemUI(false);
            } else if (isSubscriber) {
                // NEW: Directly load Subscriber UI after registration
                loadFragment(new UserDashboardFragment(), false);
                toggleSystemUI(false); // Hide Admin Nav
            } else {
                // Default (Staff Login / Admin view)
                loadFragment(new DashboardFragment(), false);
                toggleSystemUI(true);
            }
        }

        // 3. Profile Click Listener
        ivProfile.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Profile feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        // 4. Bottom Navigation Listener
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

    /**
     * Use this method to show/hide the Navigation and Header
     * Call toggleSystemUI(true) after a successful login!
     */
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