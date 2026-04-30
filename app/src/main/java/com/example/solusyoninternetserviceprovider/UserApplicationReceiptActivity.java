package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class UserApplicationReceiptActivity extends AppCompatActivity {

    private RelativeLayout statusContainer;
    private TextView tvStatusTitle, tvStatusDesc;
    private ImageView statusIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_application_receipt);

        // 1. Initialize ALL Views (Including the status section views)
        statusContainer = findViewById(R.id.statusContainer);
        tvStatusTitle = findViewById(R.id.tvStatusTitle);
        tvStatusDesc = findViewById(R.id.tvStatusDesc);
        statusIcon = findViewById(R.id.statusIcon);

        TextView tvReceiptID = findViewById(R.id.tvReceiptID);
        TextView tvDateIssued = findViewById(R.id.tvDateIssued);
        TextView tvReceiptName = findViewById(R.id.tvReceiptName);
        TextView tvReceiptPhone = findViewById(R.id.tvReceiptPhone);
        TextView tvPlanName = findViewById(R.id.tvPlanName);
        TextView tvPaymentName = findViewById(R.id.tvPaymentName);
        ImageView imgPaymentLogo = findViewById(R.id.imgPaymentLogo);

        // 2. Get Data from Intent and fill the receipt
        Bundle data = getIntent().getExtras();
        if (data != null) {
            tvReceiptID.setText(data.getString("appId"));
            tvDateIssued.setText(data.getString("date"));
            tvReceiptName.setText(data.getString("fullName"));
            tvReceiptPhone.setText(data.getString("phone"));
            tvPlanName.setText(data.getString("plan") + " Fiber Plan");

            String payment = data.getString("payment");
            tvPaymentName.setText(payment);

            // Update payment icon
            if (payment != null && payment.contains("Gcash")) {
                imgPaymentLogo.setImageResource(R.drawable.gcash);
            } else if (payment != null && payment.contains("Maya")) {
                imgPaymentLogo.setImageResource(R.drawable.ic_maya);
            }
        }

        // 3. Set Status to Pending (This will now work because views are initialized)
        updateStatus("pending");
    }

    private void updateStatus(String status) {
        if (status == null) return;

        switch (status.toLowerCase()) {
            case "approved":
                statusContainer.setBackgroundResource(R.drawable.bg_status_approved);
                tvStatusTitle.setText("Approved");
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.green_dark));
                tvStatusDesc.setText("Your internet service request has\nbeen processed and verified.");
                statusIcon.setImageResource(R.drawable.ic_check_circle);
                break;

            case "pending":
                // Using setBackgroundResource or setBackgroundColor depending on your drawable
                statusContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow_light));
                tvStatusTitle.setText("Pending");
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.yellow_dark));
                tvStatusDesc.setText("Your application is currently under\nreview by our technical team.");
                statusIcon.setImageResource(R.drawable.ic_clock);
                break;

            case "denied":
                statusContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.red_light));
                tvStatusTitle.setText("Denied");
                tvStatusTitle.setTextColor(ContextCompat.getColor(this, R.color.red_dark));
                tvStatusDesc.setText("We couldn't verify your details.\nPlease contact support for help.");
                statusIcon.setImageResource(R.drawable.ic_error);
                break;
        }
    }
}