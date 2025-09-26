package com.example.register_page;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput, confirmPasswordInput;
    private MaterialButton createBtn, clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        emailInput = findViewById(R.id.Email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.ConfirmPassword_input);
        createBtn = findViewById(R.id.create_btn);
        clearBtn = findViewById(R.id.clear_btn);

        // Clear button functionality
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailInput.setText("");
                passwordInput.setText("");
                confirmPasswordInput.setText("");
            }
        });

        // Create Account button functionality
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                // Email validation
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Enter a valid email");
                    return;
                }

                // Password validation
                if (!isValidPassword(password)) {
                    passwordInput.setError("Password must be at least 8 characters, include 1 uppercase letter and 1 symbol");
                    return;
                }

                // Confirm password match
                if (!password.equals(confirmPassword)) {
                    confirmPasswordInput.setError("Passwords do not match");
                    return;
                }

                // Success
                Toast.makeText(MainActivity.this, "Account created successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Password validation method
    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Pattern symbolPattern = Pattern.compile("[^a-zA-Z0-9]");
        return upperCasePattern.matcher(password).find() && symbolPattern.matcher(password).find();
    }
}




