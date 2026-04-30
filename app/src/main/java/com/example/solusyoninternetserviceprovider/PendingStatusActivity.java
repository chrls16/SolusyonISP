package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PendingStatusActivity extends AppCompatActivity {

    private RecyclerView rvPendingRequests;
    private PendingRequestAdapter adapter;
    private List<PendingRequestModel> requestList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_status);

        rvPendingRequests = findViewById(R.id.rvPendingRequests);
        rvPendingRequests.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        adapter = new PendingRequestAdapter(requestList, this);
        rvPendingRequests.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ServiceApplications");

        fetchPendingApplications();
    }

    // Inside PendingStatusActivity.java -> fetchPendingApplications()
    private void fetchPendingApplications() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        PendingRequestModel model = ds.getValue(PendingRequestModel.class);
                        if (model != null) {
                            model.setUid(ds.getKey()); // This is the user's UID
                            if ("pending".equalsIgnoreCase(model.getStatus())) {
                                requestList.add(model);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();

                    // If it's still empty, let's notify the user
                    if (requestList.isEmpty()) {
                        Toast.makeText(PendingStatusActivity.this, "No pending applications found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PendingStatusActivity.this, "Database node 'ServiceApplications' is empty.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PendingStatusActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}