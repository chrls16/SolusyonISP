package com.example.solusyoninternetserviceprovider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {
    private List<BillingModel> list;

    public BillingAdapter(List<BillingModel> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billing_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillingModel model = list.get(position);
        holder.tvName.setText(model.getName());
        holder.tvAccount.setText(model.getAccountNo());
        holder.tvPlan.setText(model.getPlanName() + " " + model.getPlanSpeed());
        holder.tvPlanType.setText(model.getPlanType());
        holder.tvInitials.setText(model.getInitials());
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAccount, tvPlan, tvPlanType, tvInitials;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAccount = itemView.findViewById(R.id.tvAccount);
            tvPlan = itemView.findViewById(R.id.tvPlan);
            tvPlanType = itemView.findViewById(R.id.tvPlanType);
            tvInitials = itemView.findViewById(R.id.tvInitials);
        }
    }
}