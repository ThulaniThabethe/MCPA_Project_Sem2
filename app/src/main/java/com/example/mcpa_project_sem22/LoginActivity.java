package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcpa_project_sem22.dao.UserDAO;
import com.example.mcpa_project_sem22.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        userDAO = new UserDAO(this);
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        Log.d("LoginActivity", "Attempting login with email: " + email);

        if (validateInput(email, password)) {
            User user = userDAO.loginUser(email, password);
            
            if (user != null) {
                Log.d("LoginActivity", "Login successful for user: " + user.getEmail());
                // Save user session
                saveUserSession(user);
                
                Toast.makeText(this, "Login successful! Welcome " + user.getFirstName(), 
                    Toast.LENGTH_SHORT).show();
                
                // Navigate to home activity
                Intent intent = new Intent(LoginActivity.this, MCPA_Home.class);

                startActivity(intent);
                finish();
            } else {
                Log.e("LoginActivity", "Login failed: Invalid email or password");
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            emailEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void saveUserSession(User user) {
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", user.getUserId());
        editor.putString("user_email", user.getEmail());
        editor.putString("user_first_name", user.getFirstName());
        editor.putString("user_last_name", user.getLastName());
        editor.putBoolean("is_logged_in", true);
        editor.apply();
    }
}