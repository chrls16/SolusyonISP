package com.example.solusyoninternetserviceprovider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserTermsFragment extends Fragment {

    private CheckBox cbAgree;
    private Button btnBack, btnAgreeSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_terms, container, false);

        // 1. Initialize Views
        cbAgree = view.findViewById(R.id.cbAgree);
        btnBack = view.findViewById(R.id.btnBack);
        btnAgreeSubmit = view.findViewById(R.id.btnAgreeSubmit);

        // 2. Initial State (Button disabled until checkbox is checked)
        btnAgreeSubmit.setEnabled(false);
        btnAgreeSubmit.setAlpha(0.5f);

        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnAgreeSubmit.setEnabled(isChecked);
            btnAgreeSubmit.setAlpha(isChecked ? 1.0f : 0.5f);
        });

        // 3. Navigation Listeners
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnAgreeSubmit.setOnClickListener(v -> performFinalSubmission());

        return view;
    }

    private void performFinalSubmission() {
        Bundle bundle = getArguments();

        // DEBUG: If this shows up, the problem is in UserDashboardFragment (data not sent)
        if (bundle == null) {
            Toast.makeText(getContext(), "Error: Application data missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnAgreeSubmit.setEnabled(false); // Prevent double submission
        btnAgreeSubmit.setText("Submitting...");

        // 1. Generate Application ID and Date
        String appId = "SOL-2026-" + (int)(Math.random() * 900000 + 100000);
        String dateIssued = new java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(new java.util.Date());

        // 2. Prepare Data for Firebase
        Map<String, Object> appData = new HashMap<>();
        appData.put("applicationId", appId);
        appData.put("date", dateIssued);
        appData.put("fullName", bundle.getString("fullName"));
        appData.put("phone", bundle.getString("phone"));
        appData.put("barangay", bundle.getString("barangay"));
        appData.put("plan", bundle.getString("plan"));
        appData.put("payment", bundle.getString("payment"));
        appData.put("status", "pending");

        String uid = FirebaseAuth.getInstance().getUid();

        if (uid == null) {
            Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Save to Firebase
        FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("ServiceApplications")
                .child(uid)
                .setValue(appData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 4. Launch Receipt Activity
                        Intent intent = new Intent(getContext(), UserApplicationReceiptActivity.class);
                        intent.putExtras(bundle); // Pass original data
                        intent.putExtra("appId", appId); // Pass generated ID
                        intent.putExtra("date", dateIssued); // Pass generated Date
                        startActivity(intent);

                        // Close the host Activity so they can't go back to the terms
                        if (getActivity() != null) getActivity().finish();
                    } else {
                        btnAgreeSubmit.setEnabled(true);
                        btnAgreeSubmit.setText("AGREE AND SUBMIT");

                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown Error";
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }
}