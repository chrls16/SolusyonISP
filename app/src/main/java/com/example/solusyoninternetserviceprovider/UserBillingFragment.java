package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class UserBillingFragment extends Fragment {

    private RecyclerView rvActivity;
    private ActivityAdapter adapter;
    private List<UserActivityItem> billingList; // Changed to UserActivityItem

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_billing, container, false);

        rvActivity = view.findViewById(R.id.rvRecentActivity);
        rvActivity.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize list using the new class name
        billingList = new ArrayList<>();
        billingList.add(new UserActivityItem("Monthly Service - Sept", "Invoice #INV-2023-09", "$89.90", "UNPAID"));
        billingList.add(new UserActivityItem("Late Fee Penalty", "Adjustment", "$34.60", "UNPAID"));
        billingList.add(new UserActivityItem("Monthly Service - Aug", "Invoice #INV-2023-08", "$89.90", "PAID"));

        adapter = new ActivityAdapter(billingList);
        rvActivity.setAdapter(adapter);

        return view;
    }
}