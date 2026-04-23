package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.view.View;
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

        statusContainer = findViewById(R.id.statusContainer);
        tvStatusTitle = findViewById(R.id.tvStatusTitle);
        tvStatusDesc = findViewById(R.id.tvStatusDesc);
        statusIcon = findViewById(R.id.statusIcon);

        // Example: Update UI based on status (normally you'd pass this via Intent)
        updateStatus("approved");
    }

    private void updateStatus(String status) {
        switch (status.toLowerCase()) {
            case "approved":
                statusContainer.setBackgroundResource(R.drawable.bg_status_approved);
                tvStatusTitle.setText("Approved");
                tvStatusTitle.setTextColor(getColor(R.color.green_dark));
                tvStatusDesc.setText("Your internet service request has\nbeen processed and verified.");
                statusIcon.setImageResource(R.drawable.ic_check_circle);
                break;

            case "pending":
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