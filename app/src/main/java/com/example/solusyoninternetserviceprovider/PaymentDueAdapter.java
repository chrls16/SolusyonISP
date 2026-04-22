package com.example.solusyoninternetserviceprovider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PaymentDueAdapter extends RecyclerView.Adapter<PaymentDueAdapter.ViewHolder> {

    private List<PaymentDue> paymentDueList;

    public PaymentDueAdapter(List<PaymentDue> paymentDueList) {
        this.paymentDueList = paymentDueList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_due, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentDue data = paymentDueList.get(position);

        holder.tvName.setText(data.getName());
        holder.tvAccount.setText(data.getAccountNo());
        holder.tvAmount.setText(data.getAmount());
        holder.tvDate.setText(data.getDueDate());
        holder.tvInitials.setText(data.getInitials());
    }

    @Override
    public int getItemCount() {
        return paymentDueList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAccount, tvAmount, tvDate, tvInitials;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDueName);
            tvAccount = itemView.findViewById(R.id.tvDueAccount);
            tvAmount = itemView.findViewById(R.id.tvDueAmount);
            tvDate = itemView.findViewById(R.id.tvDueDate);
            tvInitials = itemView.findViewById(R.id.tvDueInitials);
        }
    }
}