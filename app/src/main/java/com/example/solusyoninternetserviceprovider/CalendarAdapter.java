package com.example.solusyoninternetserviceprovider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final List<String> daysOfMonth;
    private final List<EventSchedule> events;
    private final int currentMonth;
    private final int currentYear;

    public CalendarAdapter(List<String> daysOfMonth, List<EventSchedule> events, int month, int year) {
        this.daysOfMonth = daysOfMonth;
        this.events = events;
        this.currentMonth = month;
        this.currentYear = year;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        holder.tvDayNumber.setText(dayText);

        // Reset UI
        holder.eventDot.setVisibility(View.INVISIBLE);
        holder.cardDayBackground.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));

        // SAFETY CHECK: Only proceed if there is a number in the cell
        if (dayText != null && !dayText.isEmpty()) {
            try {
                // Construct the date to match your Firebase format: M/d/yyyy
                String cellDate = (currentMonth + 1) + "/" + dayText + "/" + currentYear;

                if (events != null) {
                    for (EventSchedule event : events) {
                        if (event.getDate() != null && event.getDate().equals(cellDate)) {
                            holder.eventDot.setVisibility(View.VISIBLE);

                            // Set colors based on type
                            String type = event.getEventType();
                            if (type != null && type.toUpperCase().contains("INSTALLATION")) {
                                holder.eventDot.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_install));
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                // Catch any unexpected parsing errors to prevent a crash
            }
        }
    }

    @Override
    public int getItemCount() { return daysOfMonth.size(); }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDayNumber;
        public MaterialCardView cardDayBackground, eventDot;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayNumber = itemView.findViewById(R.id.tvDayNumber);
            cardDayBackground = itemView.findViewById(R.id.cardDayBackground);
            eventDot = itemView.findViewById(R.id.eventDot);
        }
    }
}