package com.example.solusyoninternetserviceprovider;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SubscriberAdapter extends RecyclerView.Adapter<SubscriberAdapter.ViewHolder> {

    private List<SubscriberModel> subscriberList;

    public SubscriberAdapter(List<SubscriberModel> subscriberList) {
        this.subscriberList = subscriberList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscriber_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubscriberModel sub = subscriberList.get(position);

        // Map data to the UI elements
        holder.tvName.setText(sub.getName());
        holder.tvSubscriberId.setText("SUBSCRIBER ID: " + sub.getSubscriberId());
        holder.tvLocationCity.setText(sub.getLocationCity());
        holder.tvPlanTag.setText(sub.getPlanTag());
        holder.tvPackageTier.setText(sub.getPackageTier());
        holder.tvSpeed.setText(sub.getSpeed());
        holder.tvBilling.setText(sub.getMonthlyBilling());
        holder.tvEmail.setText(sub.getEmail());
        holder.tvPhone.setText(sub.getPhone());
        holder.tvAddress.setText(sub.getFullAddress());

        // Handle Active/Inactive logic dynamically
        if (sub.isActive()) {
            holder.tvAccountStatus.setText("ACTIVE ACCOUNT");
            holder.tvAccountStatus.setTextColor(Color.parseColor("#208B7A"));
            holder.tvAccountStatus.setBackgroundColor(Color.parseColor("#D1F0EA"));
            holder.vStatusDot.setVisibility(View.VISIBLE);
        } else {
            holder.tvAccountStatus.setText("INACTIVE ACCOUNT");
            holder.tvAccountStatus.setTextColor(Color.parseColor("#6C757D"));
            holder.tvAccountStatus.setBackgroundColor(Color.parseColor("#E9ECEF"));
            holder.vStatusDot.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return subscriberList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSubscriberId, tvAccountStatus, tvLocationCity;
        TextView tvPlanTag, tvPackageTier, tvSpeed, tvBilling;
        TextView tvEmail, tvPhone, tvAddress;
        View vStatusDot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Profile Card
            tvName = itemView.findViewById(R.id.tvName);
            tvSubscriberId = itemView.findViewById(R.id.tvSubscriberId);
            tvAccountStatus = itemView.findViewById(R.id.tvAccountStatus);
            tvLocationCity = itemView.findViewById(R.id.tvLocationCity);
            vStatusDot = itemView.findViewById(R.id.vStatusDot);

            // Plan Card
            tvPlanTag = itemView.findViewById(R.id.tvPlanTag);
            tvPackageTier = itemView.findViewById(R.id.tvPackageTier);
            tvSpeed = itemView.findViewById(R.id.tvSpeed);
            tvBilling = itemView.findViewById(R.id.tvBilling);

            // Contact Info
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);

            // Address
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }
}