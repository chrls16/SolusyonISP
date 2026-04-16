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
        // Safe layout inflation
        return inflater.inflate(R.layout.pending_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup UI Components with Null Checks
        rvPendingRequests = view.findViewById(R.id.rvPendingRequests);
        ImageView btnBack = view.findViewById(R.id.id_btnBack);

        if (rvPendingRequests == null) {
            Log.e("CRASH_FIX", "RecyclerView NOT FOUND! Check R.id.rvPendingRequests in XML");
            return;
        }

        // 2. Hide Bottom Nav
        View bottomNav = requireActivity().findViewById(R.id.bottomNavigation);
        if (bottomNav != null) bottomNav.setVisibility(View.GONE);

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
            pendingRef = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("PendingRequests");
            fetchPendingRequests();
        } catch (Exception e) {
            Log.e("CRASH_FIX", "Firebase Error: " + e.getMessage());
        }
    }

    private void fetchPendingRequests() {
        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear(); // This clears the old data
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PendingRequestModel request = ds.getValue(PendingRequestModel.class);
                    if (request != null) requestList.add(request);
                }
                // This safely refreshes the UI without index errors
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}