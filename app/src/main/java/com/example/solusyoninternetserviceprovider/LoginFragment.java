package com.example.solusyoninternetserviceprovider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private CheckBox cbTrustDevice;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword;
    private View viewSuccess;
    private View viewError;

    // Firebase Auth & Database
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide the Header and Bottom Navigation Bar on the Login Screen
        toggleSystemUI(false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://solusyon-isp-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Initialize views
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        cbTrustDevice = view.findViewById(R.id.cbTrustDevice);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        viewSuccess = view.findViewById(R.id.viewSuccess);
        viewError = view.findViewById(R.id.viewError);

        // REMEMBER ME: Load saved credentials
        SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", 0);
        if (prefs.getBoolean("rememberMe", false)) {
            etUsername.setText(prefs.getString("email", ""));
            etPassword.setText(prefs.getString("password", ""));
            cbTrustDevice.setChecked(true);
        }

        // Set click listeners
        btnLogin.setOnClickListener(v -> handleLogin());

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Password reset feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleLogin() {
        String email = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        boolean isTrusted = cbTrustDevice.isChecked();

        viewSuccess.setVisibility(View.GONE);
        viewError.setVisibility(View.GONE);

        if (email.isEmpty() || password.isEmpty()) {
            viewError.setVisibility(View.VISIBLE);
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Verifying...");

        // 1. Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Save credentials if "Remember Me" is checked
                            saveLoginPrefs(email, password, isTrusted);

                            // 2. Check Role and Redirect
                            checkUserRole(user.getUid());
                        }
                    } else {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Sign in to Dashboard");
                        viewError.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void checkUserRole(String uid) {
        // Make sure we are looking at the exact path
        mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get the role and force it to lowercase to avoid "Admin" vs "admin" issues
                    String role = snapshot.child("role").getValue(String.class);
                    if (role != null) role = role.toLowerCase().trim();

                    // DEBUG TOAST: This will show you what the app sees
                    Toast.makeText(getContext(), "Role detected: [" + role + "]", Toast.LENGTH_SHORT).show();

                    viewSuccess.setVisibility(View.VISIBLE);

                    if ("subscriber".equals(role)) {
                        // If the database says "subscriber", load the Customer dashboard
                        navigateToFragment(new UserDashboardFragment(), false);
                    } else if ("admin".equals(role)) {
                        // If the database says "admin", load the Staff dashboard
                        navigateToFragment(new DashboardFragment(), true);
                    } else {
                        // If role is something else, default to Admin for now or show error
                        navigateToFragment(new DashboardFragment(), true);
                    }
                } else {
                    // User ID was not found in the "users" node
                    Toast.makeText(getContext(), "Error: User profile not found in database.", Toast.LENGTH_LONG).show();
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                btnLogin.setEnabled(true);
                Toast.makeText(getContext(), "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToFragment(Fragment fragment, boolean showAdminUI) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded()) {
                toggleSystemUI(showAdminUI);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        }, 800);
    }

    private void toggleSystemUI(boolean show) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).toggleSystemUI(show);
        }
    }

    private void saveLoginPrefs(String email, String password, boolean remember) {
        SharedPreferences loginPrefs = requireActivity().getSharedPreferences("LoginPrefs", 0);
        SharedPreferences.Editor editor = loginPrefs.edit();
        if (remember) {
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putBoolean("rememberMe", true);
        } else {
            editor.clear();
        }
        editor.apply();
    }
}