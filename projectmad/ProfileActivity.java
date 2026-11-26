package com.example.projectmad;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvEmail, tvUserType;
    private EditText etFullName, etAffiliation;
    private SwitchMaterial switchDarkMode;
    private Button btnSaveProfile, btnLogout;
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
        setContentView(R.layout.activity_profile);

        tvEmail = findViewById(R.id.tvProfileEmail);
        tvUserType = findViewById(R.id.tvProfileUserType);
        etFullName = findViewById(R.id.etProfileFullName);
        etAffiliation = findViewById(R.id.etProfileAffiliation);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);

        loadUserProfile();

        // Set initial switch state and listen for White/Black mode changes
        switchDarkMode.setChecked(sessionManager.isDarkModeEnabled());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sessionManager.setDarkModeEnabled(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        btnSaveProfile.setOnClickListener(v -> {
            String updatedName = etFullName.getText().toString().trim();
            String updatedAffiliation = etAffiliation.getText().toString().trim();

            if (TextUtils.isEmpty(updatedName)) {
                etFullName.setError("Full name cannot be empty");
                etFullName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(updatedAffiliation)) {
                etAffiliation.setError("College/faculty/role cannot be empty");
                etAffiliation.requestFocus();
                return;
            }

            sessionManager.updateProfile(updatedName, updatedAffiliation);
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserProfile() {
        tvEmail.setText("Email: " + sessionManager.getEmail());
        tvUserType.setText("Account Type: " + sessionManager.getUserType());
        etFullName.setText(sessionManager.getFullName());
        etAffiliation.setText(sessionManager.getAffiliation());
    }
}