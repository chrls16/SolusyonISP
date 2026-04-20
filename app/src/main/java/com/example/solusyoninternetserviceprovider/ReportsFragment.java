package com.example.solusyoninternetserviceprovider;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    private TextView tvTotalSubscribers, tvMonthlyRevenue;
    private TextView btnFilter6Months, btnFilter1Year;
    private DatabaseReference dbRef;
    private LineChart lineChart;
    private LinearLayout monthContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        // Initialize Views
        tvTotalSubscribers = view.findViewById(R.id.tvTotalSubscribers);
        tvMonthlyRevenue = view.findViewById(R.id.tvMonthlyRevenue);
        lineChart = view.findViewById(R.id.reportingLineChart);
        monthContainer = view.findViewById(R.id.monthContainer);

        // Filter Buttons
        btnFilter6Months = view.findViewById(R.id.btnFilter6Months);
        btnFilter1Year = view.findViewById(R.id.btnFilter1Year);

        dbRef = FirebaseDatabase.getInstance().getReference();

        setupMonthClickListeners(view);
        setupFilterClickListeners();
        setupLineChart();
        calculateStatistics();

        return view;
    }

    private void setupFilterClickListeners() {
        btnFilter6Months.setOnClickListener(v -> {
            updateFilterUI(btnFilter6Months, btnFilter1Year);
            updateChartData(6);
            Toast.makeText(getContext(), "Showing last 6 months", Toast.LENGTH_SHORT).show();
        });

        btnFilter1Year.setOnClickListener(v -> {
            updateFilterUI(btnFilter1Year, btnFilter6Months);
            updateChartData(12);
            Toast.makeText(getContext(), "Showing last year", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateFilterUI(TextView active, TextView inactive) {
        // Active Style
        active.setBackgroundResource(R.drawable.bg_filter_selected);
        active.setTextColor(Color.parseColor("#0E3C7E"));
        active.setTypeface(null, Typeface.BOLD);

        // Inactive Style
        inactive.setBackground(null);
        inactive.setTextColor(Color.parseColor("#64748B"));
        inactive.setTypeface(null, Typeface.NORMAL);
    }

    private void setupMonthClickListeners(View view) {
        int[] monthIds = {
                R.id.monthJan, R.id.monthFeb, R.id.monthMar, R.id.monthApr,
                R.id.monthMay, R.id.monthJun, R.id.monthJul, R.id.monthAug,
                R.id.monthSep, R.id.monthOct, R.id.monthNov, R.id.monthDec
        };

        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        for (int i = 0; i < monthIds.length; i++) {
            final String name = monthNames[i];
            TextView monthTv = view.findViewById(monthIds[i]);
            if (monthTv != null) {
                if (monthIds[i] == R.id.monthJun) {
                    highlightSelectedMonth(monthTv);
                }

                monthTv.setOnClickListener(v -> {
                    highlightSelectedMonth(monthTv);
                    Toast.makeText(getContext(), "Loading data for " + name, Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void highlightSelectedMonth(TextView selectedMonth) {
        for (int i = 0; i < monthContainer.getChildCount(); i++) {
            View v = monthContainer.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextColor(Color.parseColor("#64748B"));
                tv.setBackground(null);
            }
        }
        selectedMonth.setTextColor(Color.parseColor("#0E3C7E"));
        selectedMonth.setBackgroundResource(R.drawable.bg_month_selected);
    }

    private void setupLineChart() {
        updateChartData(6); // Default view
    }

    private void updateChartData(int range) {
        List<Entry> entries = new ArrayList<>();

        if (range == 6) {
            // Dummy data for 6 months
            entries.add(new Entry(0, 35f));
            entries.add(new Entry(1, 45f));
            entries.add(new Entry(2, 42f));
            entries.add(new Entry(3, 58f));
            entries.add(new Entry(4, 65f));
            entries.add(new Entry(5, 78f));
        } else {
            // Mock data for 12 months
            for (int i = 0; i < 12; i++) {
                float val = (float) (Math.random() * 40) + 40;
                entries.add(new Entry(i, val));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Subscription Growth");
        dataSet.setColor(Color.parseColor("#0E3C7E"));
        dataSet.setCircleColor(Color.parseColor("#0E3C7E"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#E0F2FE"));

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);

        lineChart.animateX(800); // Horizontal draw animation
        lineChart.invalidate();
    }

    private void calculateStatistics() {
        dbRef.child("Subscribers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded()) {
                    long count = snapshot.getChildrenCount();
                    tvTotalSubscribers.setText(String.valueOf(count));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}