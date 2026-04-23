package com.example.solusyoninternetserviceprovider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserCreateAccountActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone;
    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_create_account);

        // 1. Initialize Views
        etFullName = findViewById(R.id.regFullName);
        etEmail = findViewById(R.id.regEmail);
        etPhone = findViewById(R.id.regPhone);
        etUsername = findViewById(R.id.regUsername);
        etPassword = findViewById(R.id.regPassword);
        etConfirmPassword = findViewById(R.id.regConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // 2. Pre-fill data (Assuming data is passed from the Receipt Activity)
        // In a real scenario, use: getIntent().getStringExtra("NAME_KEY")
        setupInitialData();

        // 3. Set up Real-time Password Matching Validation
        setupPasswordValidation();

        // 4. Register Button Logic
        btnRegister.setOnClickListener(v -> {
            if (validateAllFields()) {
                processRegistration();
            }
        });
    }

    private void setupInitialData() {
        // Mock data based on your design screenshot
        etFullName.setText("Juan Dela Cruz");
        etEmail.setText("juan.delacruz@email.com");
        etPhone.setText("+63 912 345 6789");
    }

    private void setupPasswordValidation() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = etPassword.getText().toString();
                String confirmPass = etConfirmPassword.getText().toString();

                if (!confirmPass.isEmpty() && !pass.equals(confirmPass)) {
                    etConfirmPassword.setError("Passwords do not match");
                } else {
                    etConfirmPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etPassword.addTextChangedListener(watcher);
        etConfirmPassword.addTextChangedListener(watcher);
    }

    private boolean validateAllFields() {
        String username = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString();
        String confirmPass = etConfirmPassword.getText().toString();

        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            return false;
        }

        if (pass.length() < 8) {
            etPassword.setError("Password must be at least 8 characters");
            return false;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void processRegistration() {
        // Logic to save to MySQL/Firebase would go here
        Toast.makeText(this, "Registration Successful! Welcome to Solusyon.", Toast.LENGTH_LONG).show();

        // Finish activity and move to login or main dashboard
        finish();
    }
}