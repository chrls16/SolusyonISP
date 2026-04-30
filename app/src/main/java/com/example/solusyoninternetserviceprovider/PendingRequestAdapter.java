package com.example.solusyoninternetserviceprovider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {

    private List<PendingRequestModel> requestList;
    private Context context;
    private final String DB_URL = "https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app";

    public PendingRequestAdapter(List<PendingRequestModel> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PendingRequestModel request = requestList.get(position);

        // Map fields from the ServiceApplications structure
        holder.tvApplicantName.setText(request.getFullName());
        holder.tvPlanType.setText(request.getPlan() + " Fiber Plan");
        holder.tvRefNo.setText(request.getApplicationId());
        holder.tvAddress.setText(request.getBarangay());
        holder.tvContact.setText(request.getPhone());

        // Approval starts the Date/Time picker flow
        holder.btnApprove.setOnClickListener(v -> showDatePicker(request, position));

        // Rejection updates status to "denied"
        holder.btnReject.setOnClickListener(v -> updateStatus(request.getUid(), "denied"));
    }

    private void showDatePicker(PendingRequestModel request, int position) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year1;
                    showTimePicker(request, selectedDate, position);
                }, year, month, day);

        datePickerDialog.setTitle("Select Installation Date");
        datePickerDialog.show();
    }

    private void showTimePicker(PendingRequestModel request, String date, int position) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, hourOfDay, minuteOfHour) -> {
                    String amPm = (hourOfDay < 12) ? "AM" : "PM";
                    int displayHour = (hourOfDay > 12) ? hourOfDay - 12 : (hourOfDay == 0 ? 12 : hourOfDay);
                    String selectedTime = String.format(Locale.getDefault(), "%d:%02d %s", displayHour, minuteOfHour, amPm);

                    approveAndSyncToEvents(request, date, selectedTime, position);
                }, hour, minute, false);

        timePickerDialog.setTitle("Select Installation Time");
        timePickerDialog.show();
    }

    private void approveAndSyncToEvents(PendingRequestModel request, String date, String time, int position) {
        if (context == null) return;

        DatabaseReference db = FirebaseDatabase.getInstance(DB_URL).getReference();

        // 1. Create the Installation Event
        EventSchedule newEvent = new EventSchedule(
                "Installation: " + request.getFullName(),
                request.getBarangay(),
                time,
                date
        );

        // 2. Push to EventSchedule
        db.child("EventSchedule").child("Upcoming").push().setValue(newEvent)
                .addOnSuccessListener(aVoid -> {
                    // 3. Update the User's Application status to "approved"
                    updateStatus(request.getUid(), "approved");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Sync Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateStatus(String uid, String status) {
        DatabaseReference db = FirebaseDatabase.getInstance(DB_URL).getReference("ServiceApplications");

        db.child(uid).child("status").setValue(status)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String msg = status.equals("approved") ? "Scheduled & Approved!" : "Application Denied.";
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvApplicantName, tvPlanType, tvRefNo, tvAddress, tvContact;
        Button btnApprove, btnReject;

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