package com.example.solusyoninternetserviceprovider;

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

public class UserTermsFragment extends Fragment {

    private CheckBox cbAgree;
    private Button btnBack, btnAgreeSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_terms, container, false);

        // 1. Initialize Views
        cbAgree = view.findViewById(R.id.cbAgree);
        btnBack = view.findViewById(R.id.btnBack);
        btnAgreeSubmit = view.findViewById(R.id.btnAgreeSubmit);

        // 2. Initial State
        // The button should be disabled until the user checks the box
        btnAgreeSubmit.setEnabled(false);
        btnAgreeSubmit.setAlpha(0.5f); // Optional: Make it look faded when disabled

        // 3. Checkbox Listener
        cbAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btnAgreeSubmit.setEnabled(true);
                btnAgreeSubmit.setAlpha(1.0f);
            } else {
                btnAgreeSubmit.setEnabled(false);
                btnAgreeSubmit.setAlpha(0.5f);
            }
        });

        // 4. Button Click Listeners
        btnBack.setOnClickListener(v -> {
            // Navigate back to the Dashboard
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        });

        btnAgreeSubmit.setOnClickListener(v -> {
            // Process the final submission
            performFinalSubmission();
        });

        return view;
    }

    private void performFinalSubmission() {
        // Show a success message or move to the next phase of the app
        Toast.makeText(getContext(), "Application Submitted Successfully!", Toast.LENGTH_LONG).show();

        // Add your logic to send data to Firebase or your database here
    }
}