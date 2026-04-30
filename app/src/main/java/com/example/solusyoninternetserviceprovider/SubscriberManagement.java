package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private TextView tvPendingValue, tvTotalConValue, tvActiveConValue;
    private DatabaseReference mDatabase;

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

        // 1. Initialize Views
        tvPendingValue = view.findViewById(R.id.tvPendingValue);
        tvTotalConValue = view.findViewById(R.id.tvTotalConValue);
        tvActiveConValue = view.findViewById(R.id.tvActiveConValue);

        barBagumbayan = view.findViewById(R.id.barBagumbayan);
        barPalanas = view.findViewById(R.id.barPalanas);
        barPobNorte = view.findViewById(R.id.barPobNorte);
        barPobSur = view.findViewById(R.id.barPobSur);
        barTugos = view.findViewById(R.id.barTugos);

        // 2. Click Listener for Pending Setups Card
        View cardPendingSetups = view.findViewById(R.id.cardPendingSetups);
        if (cardPendingSetups != null) {
            cardPendingSetups.setOnClickListener(v -> {
                // Open the Pending Status screen
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, new PendingStatusFragment())
                        .addToBackStack(null)
                        .commit();
            });
        }

        // 3. Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        // 4. Fetch Data
        fetchPendingSetupsCount();
        fetchSubscriberStats();
        fetchGraphData();
    }

    private void fetchPendingSetupsCount() {
        // Fetch count from ServiceApplications where status is "pending"
        mDatabase.child("ServiceApplications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String status = ds.child("status").getValue(String.class);
                    if ("pending".equalsIgnoreCase(status)) {
                        count++;
                    }
                }
                if (isAdded()) {
                    tvPendingValue.setText(String.valueOf(count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchSubscriberStats() {
        // Fetch Total and Active connections
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total = 0;
                int active = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String role = ds.child("role").getValue(String.class);
                    if ("subscriber".equals(role)) {
                        total++;
                        // You can add logic here to check if they are "active" vs "disconnected"
                        active++;
                    }
                }
                if (isAdded()) {
                    tvTotalConValue.setText(String.valueOf(total));
                    tvActiveConValue.setText(String.valueOf(active));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchGraphData() {
        mDatabase.child("ServiceApplications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countBagumbayan = 0, countPalanas = 0, countPobNorte = 0, countPobSur = 0, countTugos = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String barangay = ds.child("barangay").getValue(String.class);
                    if (barangay != null) {
                        barangay = barangay.toLowerCase();
                        if (barangay.contains("bagumbayan")) countBagumbayan++;
                        else if (barangay.contains("palanas")) countPalanas++;
                        else if (barangay.contains("norte")) countPobNorte++;
                        else if (barangay.contains("sur")) countPobSur++;
                        else if (barangay.contains("tugos")) countTugos++;
                    }
                }

                int maxCount = Math.max(countBagumbayan, Math.max(countPalanas,
                        Math.max(countPobNorte, Math.max(countPobSur, countTugos))));
                if (maxCount == 0) maxCount = 1;

                if (isAdded()) {
                    setBarHeight(barBagumbayan, countBagumbayan, maxCount, 100);
                    setBarHeight(barPalanas, countPalanas, maxCount, 100);
                    setBarHeight(barPobNorte, countPobNorte, maxCount, 100);
                    setBarHeight(barPobSur, countPobSur, maxCount, 100);
                    setBarHeight(barTugos, countTugos, maxCount, 100);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void setBarHeight(View barView, int currentCount, int maxCount, int maxHeightDp) {
        float percentage = (float) currentCount / maxCount;
        if (currentCount == 0) percentage = 0.05f;
        int targetHeightDp = (int) (maxHeightDp * percentage);
        int heightInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, targetHeightDp, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) barView.getLayoutParams();
        params.height = heightInPixels;
        barView.setLayoutParams(params);
    }
}