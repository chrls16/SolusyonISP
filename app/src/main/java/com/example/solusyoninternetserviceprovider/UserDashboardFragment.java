package com.example.solusyoninternetserviceprovider;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class UserDashboardFragment extends Fragment {

    private EditText etLastName, etFirstName, etPhone;
    private Spinner spBarangay;
    private Button btnProceed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_dashboard, container, false);

        // Initialize Views
        etLastName = view.findViewById(R.id.etLastName);
        etFirstName = view.findViewById(R.id.etFirstName);
        etPhone = view.findViewById(R.id.etPhone);
        spBarangay = view.findViewById(R.id.spBarangay);
        btnProceed = view.findViewById(R.id.btnProceed);

        // Setup Barangay Spinner
        String[] barangays = {"Select Barangay", "Bagumbayan", "Palanas", "Poblacion Norte", "Poblacion Sur", "Tugos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, barangays);
        spBarangay.setAdapter(adapter);

        btnProceed.setOnClickListener(v -> validateAndProceed());

        return view;
    }

    private void validateAndProceed() {
        String phone = etPhone.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();

        // 1. Validate Phone (Exactly 11 digits)
        if (phone.length() != 11 || !phone.startsWith("09")) {
            etPhone.setError("Must be exactly 11 digits starting with 09");
            return;
        }

        // 2. Validate Barangay selection
        if (spBarangay.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select a Barangay", Toast.LENGTH_SHORT).show();
            return;
        }

        // If validation passes, capitalize names (though android:inputType="textCapWords" helps)
        String formattedName = capitalize(firstName) + " " + capitalize(lastName);

        // Proceed logic here
        Toast.makeText(getContext(), "Processing for " + formattedName, Toast.LENGTH_SHORT).show();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}