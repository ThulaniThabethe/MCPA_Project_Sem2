package com.example.mcpa_project_sem22;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText, emailEditText;
    private Button signInButton, registerButton, justBrowsingButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        signInButton = findViewById(R.id.sign_in_button);
        registerButton = findViewById(R.id.register_button);
        justBrowsingButton = findViewById(R.id.just_browsing_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (databaseHelper.checkUser(username, password)) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (databaseHelper.checkUsername(username)) {
                    Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else if (databaseHelper.checkEmail(email)) {
                    Toast.makeText(MainActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    if (databaseHelper.addUser(username, password, email)) {
                        Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to another activity or clear fields
                    } else {
                        Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        justBrowsingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
                startActivity(intent);
            }
        });
        if (databaseHelper.getAllPromotions().isEmpty()) {
            databaseHelper.addPromotion("Summer Sale", "Get up to 50% off on selected items!", "2023-06-01", "2023-08-31", "url_to_image_1");
            databaseHelper.addPromotion("Weekend Deal", "20% off on all fresh produce.", "2023-07-15", "2023-07-17", "url_to_image_2");
            databaseHelper.addPromotion("Loyalty Bonus", "Extra 10% off for loyalty members.", "2023-07-01", "2023-07-31", "url_to_image_3");
        }
    }
}