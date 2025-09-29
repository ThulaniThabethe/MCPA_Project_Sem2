package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, signupButton, guestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        guestButton = findViewById(R.id.guestButton);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, McpaSignup.class);
            startActivity(intent);
        });

        guestButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MCPA_Home.class);
            startActivity(intent);
        });
    }
}