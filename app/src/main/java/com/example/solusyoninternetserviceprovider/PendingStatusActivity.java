package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PendingStatusActivity extends AppCompatActivity {

    // Cleaned up the variable name
    private RecyclerView rvPendingRequests;
    private PendingRequestAdapter adapter;
    private List<PendingRequestModel> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_status);

        // THE FIX IS HERE: Pointing to the correct ID from your XML
        rvPendingRequests = findViewById(R.id.rvPendingRequests);
        rvPendingRequests.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        requestList.add(new PendingRequestModel("Juan Dela Cruz", "Basic Plan - ₱499", "SOL-10001", "Purok Kamagong, Poblacion Norte", "09123456789 | juan@test.com"));
        requestList.add(new PendingRequestModel("Maria Santos", "Pro Plan - ₱999", "SOL-10002", "Brgy. Bagumbayan, Paracale", "09987654321 | maria@test.com"));
        requestList.add(new PendingRequestModel("Pedro Penduko", "Standard Plan - ₱699", "SOL-10003", "Purok 1, Poblacion Sur", "09451234567 | pedro@test.com"));

        adapter = new PendingRequestAdapter(requestList, this);
        rvPendingRequests.setAdapter(adapter);
    }
}