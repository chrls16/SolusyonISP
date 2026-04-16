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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {

    private List<PendingRequestModel> requestList;
    private Context context;

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

        holder.tvApplicantName.setText(request.getName());
        holder.tvPlanType.setText(request.getPlanType());
        holder.tvRefNo.setText(request.getReferenceNo());
        holder.tvAddress.setText(request.getAddress());
        holder.tvContact.setText(request.getContact());

        holder.btnApprove.setOnClickListener(v -> showDatePicker(request, position));

        holder.btnReject.setOnClickListener(v -> {
            // Remove from Firebase directly for rejection to keep logic consistent
            DatabaseReference db = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
            db.child("PendingRequests").orderByChild("referenceNo").equalTo(request.getReferenceNo())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            for (DataSnapshot child : task.getResult().getChildren()) {
                                child.getRef().removeValue();
                            }
                        }
                    });
        });
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
        // 1. Safety Check: If the context is gone, don't even start
        if (context == null) return;

        DatabaseReference db = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        EventSchedule newEvent = new EventSchedule(
                "Installation: " + request.getName(),
                request.getAddress(),
                time,
                date
        );

        // 2. Perform Firebase tasks
        db.child("EventSchedule").child("Upcoming").push().setValue(newEvent)
                .addOnSuccessListener(aVoid -> {
                    db.child("PendingRequests").orderByChild("referenceNo").equalTo(request.getReferenceNo())
                            .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().exists()) {
                                    for (com.google.firebase.database.DataSnapshot child : task.getResult().getChildren()) {
                                        child.getRef().removeValue();
                                    }

                                    // 3. UI UPDATE FIX: Use a Post-Delayed or runOnUIThread
                                    // to let the Dialog dismiss fully before updating the list.
                                    if (context != null) {
                                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                                            Toast.makeText(context, "Successfully Scheduled!", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    if (context != null) Toast.makeText(context, "Sync Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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