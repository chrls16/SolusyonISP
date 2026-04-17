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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventSchedule> eventList;

    public EventAdapter(List<EventSchedule> eventList) {
        this.eventList = eventList;
    }

    // --- NEW METHOD: UPDATE LIST FOR FILTERING ---
    public void updateList(List<EventSchedule> newList) {
        this.eventList = newList;
        notifyDataSetChanged();
    }
    // ---------------------------------------------

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventSchedule event = eventList.get(position);

        // Safety check for null event data
        if (event == null) return;

        holder.tvEventType.setText(event.getEventType());
        holder.tvEventTitle.setText(event.getAddress());

        // COMBINE DATE AND TIME
        String eventDate = event.getDate() != null ? event.getDate() : "No Date";
        String eventTime = event.getTime() != null ? event.getTime() : "No Time";
        holder.tvEventTime.setText(eventDate + " • " + eventTime);

        // Set Colors based on event type
        String type = event.getEventType() != null ? event.getEventType() : "";

        if (type.toUpperCase().contains("INSTALLATION")) {
            holder.cardEventContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_bg_install));
            holder.dotIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_install));
            holder.tvEventType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_install));
        } else if (type.toUpperCase().contains("MAINTENANCE")) {
            holder.cardEventContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_bg_maint));
            holder.dotIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_maint));
            holder.tvEventType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_maint));
        }
    }

    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardEventContainer, dotIndicator;
        TextView tvEventType, tvEventTitle, tvEventTime;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardEventContainer = itemView.findViewById(R.id.cardEventContainer);
            dotIndicator = itemView.findViewById(R.id.dotIndicator);
            tvEventType = itemView.findViewById(R.id.tvEventType);
            tvEventTitle = itemView.findViewById(R.id.tvEventTitle);
            tvEventTime = itemView.findViewById(R.id.tvEventTime);
        }
    }
}