package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ShapeableImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Header Views
        ivProfile = findViewById(R.id.ivProfile);

        // Profile Click Listener (Placeholder for now)
        ivProfile.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Profile feature coming soon!", Toast.LENGTH_SHORT).show();
            // When developed, you can load ProfileFragment here
        });

        // Initialize Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Load the DashboardFragment by default when the activity starts
        // (Changed from LoginFragment since you are now inside the main app)
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment(), false);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_subscribers) {
                // Ensure this matches your actual Fragment class name
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

    // Helper method to load fragments with optional animation
    private void loadFragment(Fragment fragment, boolean animate) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (animate) {
            // Using subtle fade transitions for a premium feel
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}