package com.example.projectmad;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etAffiliation, etPassword, etConfirmPassword;
    private Button btnRegister, btnBackToLogin;
    private AuthSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new AuthSessionManager(this);

        etFullName = findViewById(R.id.etRegFullName);
        etEmail = findViewById(R.id.etRegEmail);
        etAffiliation = findViewById(R.id.etRegRoleOrCollege);
        etPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnRegister.setOnClickListener(v -> handleRegister());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String affiliation = etAffiliation.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("UM email is required");
            etEmail.requestFocus();
            return;
        }

        if (!sessionManager.isValidUmEmail(email)) {
            etEmail.setError("Only @siswa.um.edu.my or @um.edu.my emails are allowed");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(affiliation)) {
            etAffiliation.setError("Please enter your college, faculty, or role");
            etAffiliation.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        sessionManager.registerUser(fullName, email, affiliation, password);
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}