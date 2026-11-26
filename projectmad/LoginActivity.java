package com.example.projectmad;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToRegister;
    private AuthSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new AuthSessionManager(this);
        AppCompatDelegate.setDefaultNightMode(
                sessionManager.isDarkModeEnabled()
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Redirect straight to profile if already signed in
        if (sessionManager.isLoggedIn()) {
            openProfile();
            finish();
            return;
        }

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> handleLogin());

        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!sessionManager.isValidUmEmail(email)) {
            etEmail.setError("Please enter a valid UM email (@siswa.um.edu.my or @um.edu.my)");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (sessionManager.authenticate(email, password)) {
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
            openProfile();
            finish();
        } else {
            Toast.makeText(this, "Invalid UM email or password. Register if you don't have an account.", Toast.LENGTH_LONG).show();
        }
    }

    private void openProfile() {
        // Replace ProfileActivity.class with your main dashboard activity once integrated
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}