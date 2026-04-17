package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Load the LoginFragment when the activity first starts
        if (savedInstanceState == null) {
            loadFragment(new LoginFragment(), false); // False = no animation on first load
        }

        // Listen for clicks on the Bottom Navigation Bar
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_subscribers) {
                selectedFragment = new SubscriberManagement();
            } else if (itemId == R.id.nav_billing) {
                //selectedFragment = new BillingFragment(); // Uncomment when you build this
                return true;
            } else if (itemId == R.id.nav_reports) {
                // selectedFragment = new ReportsFragment(); // Uncomment when you build this
                return true;
            }

            // Swap the fragment inside the container
            if (selectedFragment != null) {
                loadFragment(selectedFragment, true); // True = fade animation between tabs!
            }

            return true;
        });
    }

    // Helper method to load fragments with optional animation
    private void loadFragment(Fragment fragment, boolean animate) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}