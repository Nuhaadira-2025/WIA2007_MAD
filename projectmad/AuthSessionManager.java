package com.example.projectmad;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
public class AuthSessionManager {

    private static final String PREF_NAME = "module1_auth_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_REGISTERED_EMAIL = "registered_email";
    private static final String KEY_REGISTERED_PASSWORD = "registered_password";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_AFFILIATION = "affiliation";
    private static final String KEY_USER_TYPE = "user_type";

    private final SharedPreferences prefs;

    public AuthSessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isValidUmEmail(String email) {
        if (email == null) return false;
        String trimmed = email.trim().toLowerCase();
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) {
            return false;
        }
        return trimmed.endsWith("@siswa.um.edu.my") || trimmed.endsWith("@um.edu.my");
    }

    public String inferUserType(String email) {
        if (email == null) return "UM User";
        String lower = email.trim().toLowerCase();
        if (lower.endsWith("@siswa.um.edu.my")) {
            return "UM Student";
        } else if (lower.endsWith("@um.edu.my")) {
            return "UM Staff / Faculty";
        }
        return "UM User";
    }

    public void registerUser(String fullName, String email, String affiliation, String password) {
        String cleanEmail = email.trim().toLowerCase();
        prefs.edit()
                .putString(KEY_FULL_NAME, fullName.trim())
                .putString(KEY_REGISTERED_EMAIL, cleanEmail)
                .putString(KEY_AFFILIATION, affiliation.trim())
                .putString(KEY_REGISTERED_PASSWORD, password)
                .putString(KEY_USER_TYPE, inferUserType(cleanEmail))
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }

    public boolean authenticate(String email, String password) {
        if (!isValidUmEmail(email) || password == null || password.isEmpty()) {
            return false;
        }
        String cleanEmail = email.trim().toLowerCase();
        String savedEmail = prefs.getString(KEY_REGISTERED_EMAIL, "");
        String savedPassword = prefs.getString(KEY_REGISTERED_PASSWORD, "");

        boolean matches = cleanEmail.equals(savedEmail) && password.equals(savedPassword);
        if (matches) {
            prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply();
        }
        return matches;
    }

    public void updateProfile(String fullName, String affiliation) {
        prefs.edit()
                .putString(KEY_FULL_NAME, fullName.trim())
                .putString(KEY_AFFILIATION, affiliation.trim())
                .apply();
    }

    public void logout() {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_REGISTERED_EMAIL, "");
    }

    public String getAffiliation() {
        return prefs.getString(KEY_AFFILIATION, "");
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, "UM User");
    }

    private static final String KEY_DARK_MODE = "dark_mode_enabled";

    public void setDarkModeEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return prefs.getBoolean(KEY_DARK_MODE, false); // false = White Mode default
    }
}
