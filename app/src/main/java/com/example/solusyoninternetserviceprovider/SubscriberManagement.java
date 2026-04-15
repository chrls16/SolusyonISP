package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

public class SubscriberManagement extends AppCompatActivity {

    // View references for the Barangay Bar Graph
    private View barBagumbayan, barPalanas, barPobNorte, barPobSur, barTugos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscriber_management);

        // 1. Initialize Barangay Bar Views
        barBagumbayan = findViewById(R.id.barBagumbayan);
        barPalanas = findViewById(R.id.barPalanas);
        barPobNorte = findViewById(R.id.barPobNorte);
        barPobSur = findViewById(R.id.barPobSur);
        barTugos = findViewById(R.id.barTugos);

        // 2. Set heights dynamically (Mock data for subscribers)
        // Values are in pixels. Consider converting from DP for consistency.
        updateBarHeight(barBagumbayan, 280);
        updateBarHeight(barPalanas, 160);
        updateBarHeight(barPobNorte, 240);
        updateBarHeight(barPobSur, 200);
        updateBarHeight(barTugos, 120);

        // Note: For the Payment and Events lists, you can later implement
        // RecyclerView Adapters if you want them to be scrollable and dynamic.
    }

    /**
     * Helper method to set the height of a View (Bar) programmatically
     */
    private void updateBarHeight(View bar, int heightPx) {
        ViewGroup.LayoutParams params = bar.getLayoutParams();
        params.height = heightPx;
        bar.setLayoutParams(params);
    }
}