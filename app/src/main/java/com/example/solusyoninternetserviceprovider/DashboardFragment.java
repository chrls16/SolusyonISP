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
    private List<EventSchedule> eventList; // Master list from Firebase
    private List<EventSchedule> displayedEvents; // List currently shown in RecyclerView

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

        // --- NEW CODE: SHOW THE NAV BAR ---
        if (isAdded() && getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottomNavigation);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.VISIBLE);
            }
        }

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

        // 3. Initialize Lists
        eventList = new ArrayList<>();
        displayedEvents = new ArrayList<>(); // Start with an empty list for the UI
        paymentList = new ArrayList<>();

        // 4. Calendar Logic
        calendar = Calendar.getInstance();
        setupCalendarGrid();

        btnPrevMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            setupCalendarGrid();
        });

        btnNextMonth.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            setupCalendarGrid();
        });

        // 5. Event List Setup - Use displayedEvents (empty) instead of eventList (master)
        rvEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventAdapter = new EventAdapter(displayedEvents);
        rvEvents.setAdapter(eventAdapter);

        // 6. Payments List Setup
        rvPaymentsDue.setLayoutManager(new LinearLayoutManager(requireContext()));
        paymentAdapter = new PaymentDueAdapter(paymentList);
        rvPaymentsDue.setAdapter(paymentAdapter);

        // 7. Fetch Data
        fetchPaymentsData();
        fetchEventsData();
    }

    // --- NEW METHOD: FILTER EVENTS BY DATE ---
    public void filterEvents(String selectedDate) {
        if (!isAdded() || eventList == null) return;

        displayedEvents.clear();
        for (EventSchedule event : eventList) {
            if (event.getDate() != null && event.getDate().equals(selectedDate)) {
                displayedEvents.add(event);
            }
        }

        if (eventAdapter != null) {
            eventAdapter.notifyDataSetChanged();
        }

        if (displayedEvents.isEmpty()) {
            Toast.makeText(getContext(), "No appointments for " + selectedDate, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCalendarGrid() {
        if (!isAdded() || getContext() == null) return;

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvCurrentMonthYear.setText(sdf.format(calendar.getTime()));

        List<String> daysInMonth = new ArrayList<>();
        Calendar calcCal = (Calendar) calendar.clone();
        calcCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calcCal.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInTotal = calcCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysInMonth.add("");
        }
        for (int i = 1; i <= daysInTotal; i++) {
            daysInMonth.add(String.valueOf(i));
        }

        rvCalendarGrid.setLayoutManager(new GridLayoutManager(getContext(), 7));
        // Pass 'this' so the CalendarAdapter can call filterEvents()
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, eventList,
                calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), this);
        rvCalendarGrid.setAdapter(calendarAdapter);
    }

    private void fetchEventsData() {
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) return;

                eventList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    EventSchedule event = ds.getValue(EventSchedule.class);
                    if (event != null) eventList.add(event);
                }

                // Do NOT notify eventAdapter here so the list stays hidden
                // until a date is clicked. Only refresh the calendar dots.
                setupCalendarGrid();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void fetchPaymentsData() {
        paymentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) return;
                paymentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PaymentDue payment = ds.getValue(PaymentDue.class);
                    if (payment != null) paymentList.add(payment);
                }
                if (paymentAdapter != null) paymentAdapter.notifyDataSetChanged();
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}