package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BillingFragment extends Fragment {

    private RecyclerView recyclerView;
    private BillingAdapter adapter;
    private List<BillingModel> fullList = new ArrayList<>();

    // UI Elements for the Filter Tabs
    private TextView tabAll, tabPaid, tabPending;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        // 1. Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvBillingHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false); // Smooth scrolling inside ScrollView

        // 2. Initialize Tab Views (The IDs must exist in fragment_billing.xml)
        tabAll = view.findViewById(R.id.tabAll);
        tabPaid = view.findViewById(R.id.tabPaid);
        tabPending = view.findViewById(R.id.tabPending);

        // 3. Set Click Listeners for Filters
        tabAll.setOnClickListener(v -> handleTabClick(tabAll, "All"));
        tabPaid.setOnClickListener(v -> handleTabClick(tabPaid, "Paid"));
        tabPending.setOnClickListener(v -> handleTabClick(tabPending, "Pending"));

        // 4. Load Initial Data
        loadData();

        return view;
    }

    private void loadData() {
        fullList.clear();

        // Mock data matching your design screenshots
        // Parameters: Name, Account, Plan, Speed, Type, Status
        fullList.add(new BillingModel("Alex Rivera", "ACC-902341", "Fiber Unlimited", "100Mbps", "Residential", "Paid"));
        fullList.add(new BillingModel("Maria Delgado", "ACC-904552", "Fiber Enterprise", "500Mbps", "Business", "Pending"));
        fullList.add(new BillingModel("James Kim", "ACC-901128", "Fiber Lite", "50Mbps", "Residential", "Paid"));
        fullList.add(new BillingModel("Sarah Lee", "ACC-908876", "Fiber Unlimited", "200Mbps", "Residential", "Pending"));

        // Default view: Show everything and set "All" tab as active
        filter("All");
    }

    private void handleTabClick(TextView selectedTab, String status) {
        // Reset all tabs to the inactive look
        resetTabStyles();

        // Apply active look to the clicked tab
        selectedTab.setBackgroundResource(R.drawable.bg_tab_active);
        selectedTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary));

        // Filter the RecyclerView list
        filter(status);
    }

    private void resetTabStyles() {
        // Remove background and set text color to inactive for all tabs
        tabAll.setBackground(null);
        tabPaid.setBackground(null);
        tabPending.setBackground(null);

        int inactiveColor = ContextCompat.getColor(requireContext(), R.color.tab_text_inactive);
        tabAll.setTextColor(inactiveColor);
        tabPaid.setTextColor(inactiveColor);
        tabPending.setTextColor(inactiveColor);
    }

    private void filter(String status) {
        List<BillingModel> filteredList = new ArrayList<>();

        for (BillingModel item : fullList) {
            // Logic: If "All" is selected, add everything. Otherwise, match the status.
            if (status.equalsIgnoreCase("All") || item.getStatus().equalsIgnoreCase(status)) {
                filteredList.add(item);
            }
        }

        // Update the adapter with the new list
        adapter = new BillingAdapter(filteredList);
        recyclerView.setAdapter(adapter);
    }
}