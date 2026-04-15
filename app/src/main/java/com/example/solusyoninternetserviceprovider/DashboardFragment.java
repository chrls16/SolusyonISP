package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    // Top UI
    private ImageView ivMenu, ivNotifications;
    private MaterialButton btnDispatch, btnViewList;
    private LinearLayout spinnerMonth;

    // Payments
    private RecyclerView rvPaymentsDue;
    private PaymentDueAdapter paymentAdapter;
    private List<PaymentDue> paymentList;

    // Events
    private RecyclerView rvEvents;
    private EventAdapter eventAdapter;
    private List<EventSchedule> eventList;

    // Calendar
    private RecyclerView rvCalendarGrid;
    private TextView tvCurrentMonthYear;
    private ImageView btnPrevMonth, btnNextMonth;
    private Calendar calendar;

    // Database
    private DatabaseReference paymentRef, eventRef;

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize DB
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app");
        paymentRef = database.getReference("PaymentDue/Late");
        eventRef = database.getReference("EventSchedule/Upcoming");

        // 2. UI Setup
        tvCurrentMonthYear = view.findViewById(R.id.tvCurrentMonthYear);
        btnPrevMonth = view.findViewById(R.id.btnPrevMonth);
        btnNextMonth = view.findViewById(R.id.btnNextMonth);
        rvCalendarGrid = view.findViewById(R.id.rvCalendarGrid);
        rvEvents = view.findViewById(R.id.rvEvents);
        rvPaymentsDue = view.findViewById(R.id.rvPaymentsDue);

        // 3. CRITICAL FIX: Initialize the empty lists FIRST!
        eventList = new ArrayList<>();
        paymentList = new ArrayList<>();

        // 4. Calendar Logic (Now it's safe to call this because eventList exists)
        calendar = Calendar.getInstance(); // Sets to today's date
        setupCalendarGrid();

        btnPrevMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            setupCalendarGrid();
        });

        btnNextMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            setupCalendarGrid();
        });

        // 5. Event List Setup
        rvEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventAdapter = new EventAdapter(eventList);
        rvEvents.setAdapter(eventAdapter);

        // 6. Payments List Setup
        rvPaymentsDue.setLayoutManager(new LinearLayoutManager(requireContext()));
        paymentAdapter = new PaymentDueAdapter(paymentList);
        rvPaymentsDue.setAdapter(paymentAdapter);

        // 7. Fetch Data from Firebase
        fetchPaymentsData();
        fetchEventsData();
    }

    private void setupCalendarGrid() {
        // Format title (e.g., "April 2026")
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvCurrentMonthYear.setText(sdf.format(calendar.getTime()));

        List<String> daysInMonth = new ArrayList<>();

        // Find out what day of the week the 1st falls on
        Calendar calcCal = (Calendar) calendar.clone();
        calcCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calcCal.get(Calendar.DAY_OF_WEEK) - 1; // 0 = Sunday
        int daysInTotal = calcCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add empty spaces before the 1st of the month
        for (int i = 0; i < firstDayOfWeek; i++) {
            daysInMonth.add("");
        }

        // Add the actual days
        for (int i = 1; i <= daysInTotal; i++) {
            daysInMonth.add(String.valueOf(i));
        }

        // Set adapter using 7 columns
        rvCalendarGrid.setLayoutManager(new GridLayoutManager(requireContext(), 7));
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, eventList, calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        rvCalendarGrid.setAdapter(calendarAdapter);
    }

    private void fetchEventsData() {
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    EventSchedule event = ds.getValue(EventSchedule.class);
                    if (event != null) eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
                setupCalendarGrid(); // Refresh calendar to draw dots based on new data!
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchPaymentsData() {
        paymentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                paymentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PaymentDue payment = ds.getValue(PaymentDue.class);
                    if (payment != null) paymentList.add(payment);
                }
                paymentAdapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}