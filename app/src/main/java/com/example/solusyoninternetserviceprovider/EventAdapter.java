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

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventSchedule event = eventList.get(position);

        holder.tvEventType.setText(event.getEventType());
        holder.tvEventTitle.setText(event.getAddress());

        // COMBINE DATE AND TIME
        String eventDate = event.getDate() != null ? event.getDate() : "No Date";
        String eventTime = event.getTime() != null ? event.getTime() : "No Time";
        holder.tvEventTime.setText(eventDate + " • " + eventTime);

        // Set Colors
        if ("INSTALLATION".equalsIgnoreCase(event.getEventType())) {
            holder.cardEventContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_bg_install));
            holder.dotIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_install));
            holder.tvEventType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_install));
        } else if ("MAINTENANCE".equalsIgnoreCase(event.getEventType())) {
            holder.cardEventContainer.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_bg_maint));
            holder.dotIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_maint));
            holder.tvEventType.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.event_text_maint));
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
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