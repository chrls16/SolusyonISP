package com.example.solusyoninternetserviceprovider;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {

    private List<BillingModel> billingList;

    public BillingAdapter(List<BillingModel> billingList) {
        this.billingList = billingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_billing_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BillingModel billing = billingList.get(position);

        // Replace spaces with newlines to match the wrapped look in your design
        holder.tvInvoiceNo.setText(billing.getInvoiceNo().replace("-", "-\n"));
        holder.tvDate.setText(billing.getDate().replace(" ", " \n"));
        holder.tvAmount.setText(billing.getAmount());
        holder.tvStatus.setText(billing.getStatus());

        // Optional: Handle UNPAID status colors dynamically
        if (billing.getStatus().equalsIgnoreCase("UNPAID")) {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828")); // Dark Red
            // You would normally create a bg_badge_unpaid.xml for the background here
        }
    }

    @Override
    public int getItemCount() {
        return billingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvoiceNo, tvDate, tvAmount, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInvoiceNo = itemView.findViewById(R.id.tvInvoiceNo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}