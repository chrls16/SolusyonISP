package com.example.solusyoninternetserviceprovider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaymentDueAdapter extends RecyclerView.Adapter<PaymentDueAdapter.PaymentViewHolder> {

    private List<PaymentDue> paymentList;

    public PaymentDueAdapter(List<PaymentDue> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_due, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        PaymentDue payment = paymentList.get(position);

        holder.tvAvatar.setText(payment.getAvatarInitials());
        holder.tvName.setText(payment.getName());
        holder.tvAccountDetails.setText(payment.getAccountDetails());
        holder.tvAmount.setText(payment.getAmount());
        holder.tvOverdueStatus.setText(payment.getOverdueStatus());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvName, tvAccountDetails, tvAmount, tvOverdueStatus;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAvatar = itemView.findViewById(R.id.tvAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvAccountDetails = itemView.findViewById(R.id.tvAccountDetails);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvOverdueStatus = itemView.findViewById(R.id.tvOverdueStatus);
        }
    }
}