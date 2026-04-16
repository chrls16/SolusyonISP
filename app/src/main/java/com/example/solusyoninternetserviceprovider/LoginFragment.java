package com.example.solusyoninternetserviceprovider;

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

public class LoginFragment extends Fragment {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private CheckBox cbTrustDevice;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword;
    private View viewSuccess;
    private View viewError;

    // Firebase Auth instance
    private FirebaseAuth mAuth;

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

        // Hide the Bottom Navigation Bar on the Login Screen
        View bottomNav = requireActivity().findViewById(R.id.bottomNavigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        cbTrustDevice = view.findViewById(R.id.cbTrustDevice);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        viewSuccess = view.findViewById(R.id.viewSuccess);
        viewError = view.findViewById(R.id.viewError);

        // Set click listeners
        btnLogin.setOnClickListener(v -> handleLogin());

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Forgot Password clicked", Toast.LENGTH_SHORT).show();
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

        // Disable button while loading
        btnLogin.setEnabled(false);
        btnLogin.setText("Signing in...");

        // Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    // Re-enable button
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Sign in to Dashboard");

                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        viewSuccess.setVisibility(View.VISIBLE);

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                            transaction.replace(R.id.fragment_container, new DashboardFragment());
                            transaction.commit();
                        }, 800);

                    } else {
                        // If sign in fails, display a message to the user.
                        viewError.setVisibility(View.VISIBLE);
                    }
                });
    }
}