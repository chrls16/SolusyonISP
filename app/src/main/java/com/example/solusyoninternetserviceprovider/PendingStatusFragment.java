package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingStatusFragment extends Fragment {

    private RecyclerView rvPendingRequests;
    private PendingRequestAdapter adapter;
    private List<PendingRequestModel> requestList;
    private DatabaseReference pendingRef;

    public PendingStatusFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pending_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup UI Components
        rvPendingRequests = view.findViewById(R.id.rvPendingRequests);
        ImageView btnBack = view.findViewById(R.id.id_btnBack);

        // 2. Hide Bottom Nav while viewing pending requests
        toggleBottomNav(false);

        // 3. Back Button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        }

        // 4. Setup RecyclerView
        rvPendingRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        requestList = new ArrayList<>();
        adapter = new PendingRequestAdapter(requestList, requireContext());
        rvPendingRequests.setAdapter(adapter);

        // 5. Firebase initialization
        try {
            pendingRef = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ServiceApplications");
            fetchPendingRequests();
        } catch (Exception e) {
            Log.e("CRASH_FIX", "Firebase Error: " + e.getMessage());
        }
    }

    private void fetchPendingRequests() {
        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        PendingRequestModel request = ds.getValue(PendingRequestModel.class);
                        if (request != null) {
                            request.setUid(ds.getKey()); // Essential for approval logic
                            if ("pending".equalsIgnoreCase(request.getStatus())) {
                                requestList.add(request);
                            }
                        }
                    }
                    if (adapter != null) adapter.notifyDataSetChanged();
                }

                if (requestList.isEmpty() && isAdded()) {
                    Toast.makeText(getContext(), "No pending setups available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded()) Log.e("DATABASE_ERROR", error.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // FIX: Ensure Bottom Navigation is visible again when returning to the previous screen
        toggleBottomNav(true);
    }

    private void toggleBottomNav(boolean show) {
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottomNavigation);
            if (bottomNav != null) {
                bottomNav.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        }
    }
}