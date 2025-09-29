package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcpa_project_sem22.dao.UserDAO;
import com.example.mcpa_project_sem22.models.User;

public class McpaSignup extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private EditText firstNameEditText, lastNameEditText, phoneEditText;
    private Button signupButton;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcpa_signup);

        initializeViews();
        userDAO = new UserDAO(this);
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        signupButton = findViewById(R.id.signupButton);
    }

    private void setupClickListeners() {
        signupButton.setOnClickListener(v -> attemptSignup());
    }

    private void attemptSignup() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (validateInput(email, password, confirmPassword, firstName, lastName)) {
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                Toast.makeText(this, "Email already exists. Please use a different email.", 
                    Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new user
            User newUser = new User(email, firstName, lastName);
            newUser.setPhoneNumber(phone);

            long userId = userDAO.registerUser(newUser, password);

            if (userId > 0) {
                Toast.makeText(this, "Registration successful! Please login.", 
                    Toast.LENGTH_SHORT).show();
                
                // Navigate to login activity
                Intent intent = new Intent(McpaSignup.this, LoginActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", 
                    Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String email, String password, String confirmPassword, 
                                String firstName, String lastName) {
        
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("First name is required");
            firstNameEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Last name is required");
            lastNameEditText.requestFocus();
            return false;
        }

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

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return false;
        }

        return true;
    }
}