package com.example.solusyoninternetserviceprovider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {

    private List<PendingRequest> requestList;
    private Context context;
    private DatabaseReference db;

    public PendingRequestAdapter(Context context, List<PendingRequest> requestList) {
        this.context = context;
        this.requestList = requestList;
        this.db = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PendingRequest request = requestList.get(position);

        holder.tvApplicantName.setText(request.getName());
        holder.tvPlanType.setText(request.getPlanType());
        holder.tvRefNo.setText(request.getReferenceNo());
        holder.tvAddress.setText(request.getAddress());
        holder.tvContact.setText(request.getPhone() + " | " + request.getEmail());

        holder.btnApprove.setOnClickListener(v -> showDatePickerForApproval(request));

        holder.btnReject.setOnClickListener(v -> {
            // Delete from PendingRequests
            db.child("PendingRequests").child(request.getFirebaseKey()).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show());
        });
    }

    private void showDatePickerForApproval(PendingRequest request) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {

            // 1. Format the chosen date to match our calendar logic (MM/DD/YYYY)
            String scheduledDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", month + 1, dayOfMonth, year);

            // 2. Create the Event object for the Calendar
            EventSchedule newEvent = new EventSchedule(
                    "INSTALLATION",
                    request.getAddress(), // Show address on calendar
                    "09:00 AM - 05:00 PM", // Default time window
                    scheduledDate
            );

            // 3. Save to EventSchedule/Upcoming
            String newEventKey = db.child("EventSchedule/Upcoming").push().getKey();
            if(newEventKey != null) {
                db.child("EventSchedule/Upcoming").child(newEventKey).setValue(newEvent)
                        .addOnSuccessListener(aVoid -> {
                            // 4. If successful, delete from PendingRequests!
                            db.child("PendingRequests").child(request.getFirebaseKey()).removeValue();
                            Toast.makeText(context, "Approved! Added to Calendar.", Toast.LENGTH_LONG).show();
                        });
            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setTitle("Select Installation Date");
        datePickerDialog.show();
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvApplicantName, tvPlanType, tvRefNo, tvAddress, tvContact;
        MaterialButton btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApplicantName = itemView.findViewById(R.id.tvApplicantName);
            tvPlanType = itemView.findViewById(R.id.tvPlanType);
            tvRefNo = itemView.findViewById(R.id.tvRefNo);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvContact = itemView.findViewById(R.id.tvContact);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}