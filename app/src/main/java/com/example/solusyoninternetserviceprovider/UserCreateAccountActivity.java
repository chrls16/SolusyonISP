package com.example.solusyoninternetserviceprovider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Firebase Imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserCreateAccountActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPhone;
    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;

    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_create_account);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
// To this (using the URL from your screenshot):
        mDatabase = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Initialize Views
        etFullName = findViewById(R.id.regFullName);
        etEmail = findViewById(R.id.regEmail);
        etPhone = findViewById(R.id.regPhone);
        etUsername = findViewById(R.id.regUsername);
        etPassword = findViewById(R.id.regPassword);
        etConfirmPassword = findViewById(R.id.regConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        setupInitialData();
        setupPasswordValidation();

        btnRegister.setOnClickListener(v -> {
            if (validateAllFields()) {
                processRegistration();
            }
        });
    }

    private void setupInitialData() {
        // Mock data based on your design screenshot
        etFullName.setHint("Juan Dela Cruz");
        etEmail.setHint("juan.delacruz@email.com");
        etPhone.setHint("+63 912 345 6789");
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
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String username = etUsername.getText().toString().trim();

        // Disable button to prevent multiple clicks
        btnRegister.setEnabled(false);
        btnRegister.setText("Creating Account...");

        // 1. Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // 2. Authentication successful, now save additional data to Realtime Database
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToDatabase(user.getUid(), fullName, email, phone, username);
                        }
                    } else {
                        // Show error if authentication fails (e.g., email already exists)
                        btnRegister.setEnabled(true);
                        btnRegister.setText("Confirm & Register");
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(UserCreateAccountActivity.this, "Auth Failed: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Inside UserCreateAccountActivity.java
    private void saveUserToDatabase(String userId, String fullName, String email, String phone, String username) {
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("fullName", fullName);
        userProfile.put("email", email);
        userProfile.put("phone", phone);
        userProfile.put("username", username);
        userProfile.put("role", "subscriber"); // Ensure correct spelling

        mDatabase.child("users").child(userId).setValue(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Welcome to Solusyon!", Toast.LENGTH_SHORT).show();

                        // Navigate to MainActivity with the Subscriber Flag
                        Intent intent = new Intent(UserCreateAccountActivity.this, MainActivity.class);
                        intent.putExtra("IS_SUBSCRIBER", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }

}
