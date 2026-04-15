package com.example.solusyoninternetserviceprovider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {

    private List<PendingRequestModel> requestList;

    public PendingRequestAdapter(List<PendingRequestModel> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PendingRequestModel request = requestList.get(position);

        holder.tvApplicantName.setText(request.getName());
        holder.tvPlanType.setText(request.getPlan());
        holder.tvRefNo.setText(request.getRefNo());
        holder.tvAddress.setText(request.getAddress());
        holder.tvContact.setText(request.getContact());

        holder.btnApprove.setOnClickListener(v -> {
            requestList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, requestList.size());
        });

        holder.btnReject.setOnClickListener(v -> {
            requestList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, requestList.size());
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvApplicantName, tvPlanType, tvRefNo, tvAddress, tvContact;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApplicantName = itemView.findViewById(R.id.tvApplicantName);
            tvPlanType = itemView.findViewById(R.id.tvPlanType);
            tvRefNo = itemView.findViewById(R.id.tvRefNo);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvContact = itemView.findViewById(R.id.tvContact);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}