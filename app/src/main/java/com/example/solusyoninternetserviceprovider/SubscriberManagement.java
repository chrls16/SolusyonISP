package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubscriberManagement extends Fragment {

    private View barBagumbayan, barPalanas, barPobNorte, barPobSur, barTugos;
    private DatabaseReference subscribersRef;

    public SubscriberManagement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.subscriber_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Show Bottom Nav
        View bottomNav = requireActivity().findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }

        // 2. Initialize Graph Views
        barBagumbayan = view.findViewById(R.id.barBagumbayan);
        barPalanas = view.findViewById(R.id.barPalanas);
        barPobNorte = view.findViewById(R.id.barPobNorte);
        barPobSur = view.findViewById(R.id.barPobSur);
        barTugos = view.findViewById(R.id.barTugos);

        // 3. Set Click Listener for Pending Setups Card
        View cardPendingSetups = view.findViewById(R.id.cardPendingSetups);
        if (cardPendingSetups != null) {
            cardPendingSetups.setOnClickListener(v -> {
                // Open the Pending Status screen
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new PendingStatusFragment())
                        .addToBackStack(null) // Allows the user to press 'Back' to return
                        .commit();
            });
        }

        // 4. Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app");
        subscribersRef = database.getReference("Subscribers");

        // 5. Fetch Data and Animate Graph
        fetchGraphData();
    }

    private void fetchGraphData() {
        subscribersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countBagumbayan = 0;
                int countPalanas = 0;
                int countPobNorte = 0;
                int countPobSur = 0;
                int countTugos = 0;

                // Loop through all subscribers and count them by their address/barangay
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Assuming your subscriber data has an "address" field
                    String address = ds.child("address").getValue(String.class);

                    if (address != null) {
                        address = address.toLowerCase();
                        if (address.contains("bagumbayan")) countBagumbayan++;
                        else if (address.contains("palanas")) countPalanas++;
                        else if (address.contains("norte")) countPobNorte++;
                        else if (address.contains("sur")) countPobSur++;
                        else if (address.contains("tugos")) countTugos++;
                    }
                }

                // Find the highest number so we can scale the graph properly
                int maxCount = Math.max(countBagumbayan, Math.max(countPalanas,
                        Math.max(countPobNorte, Math.max(countPobSur, countTugos))));

                // If database is empty, prevent dividing by zero
                if (maxCount == 0) maxCount = 1;

                // Max height of the bar graph container is 100dp
                int maxBarHeightDp = 100;

                // Set the heights!
                setBarHeight(barBagumbayan, countBagumbayan, maxCount, maxBarHeightDp);
                setBarHeight(barPalanas, countPalanas, maxCount, maxBarHeightDp);
                setBarHeight(barPobNorte, countPobNorte, maxCount, maxBarHeightDp);
                setBarHeight(barPobSur, countPobSur, maxCount, maxBarHeightDp);
                setBarHeight(barTugos, countTugos, maxCount, maxBarHeightDp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }

    // Helper method to mathematically scale the bar and convert dp to actual screen pixels
    private void setBarHeight(View barView, int currentCount, int maxCount, int maxHeightDp) {
        // Calculate what percentage of the max height this bar should be
        float percentage = (float) currentCount / maxCount;

        // Ensure there's a tiny bit of height even if count is 0 so the bar doesn't disappear completely
        if (currentCount == 0) percentage = 0.05f;

        int targetHeightDp = (int) (maxHeightDp * percentage);

        // Convert the DP value to actual Pixels for the device screen
        int heightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                targetHeightDp,
                getResources().getDisplayMetrics()
        );

        // Apply the new height to the view
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) barView.getLayoutParams();
        params.height = heightInPixels;
        barView.setLayoutParams(params);
    }
}