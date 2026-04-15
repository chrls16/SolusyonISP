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

        // Reset visibility and color
        holder.eventDot.setVisibility(View.INVISIBLE);
        holder.cardDayBackground.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));

        if (!dayText.isEmpty()) {
            // Format the current cell date to MM/DD/YYYY to match Firebase
            String cellDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", currentMonth + 1, Integer.parseInt(dayText), currentYear);

            // Check if any event falls on this date
            for (EventSchedule event : events) {
                if (event.getDate() != null && event.getDate().equals(cellDate)) {
                    holder.eventDot.setVisibility(View.VISIBLE);

                    // Set dot color based on event type
                    if ("INSTALLATION".equalsIgnoreCase(event.getEventType())) {
                        holder.eventDot.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_install));
                    } else if ("MAINTENANCE".equalsIgnoreCase(event.getEventType())) {
                        holder.eventDot.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_maint));
                    }
                    break; // Just show one dot per day for now
                }
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