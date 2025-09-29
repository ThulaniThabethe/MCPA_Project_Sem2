package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MCPA_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcpa_home);

        Button viewProfileButton = findViewById(R.id.viewProfileButton);
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MCPA_Home.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button browseProductsButton = findViewById(R.id.browseProductsButton);
        browseProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MCPA_Home.this, ProductListActivity.class);
                startActivity(intent);
            }
        });

        Button viewCartButton = findViewById(R.id.viewCartButton);
        viewCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MCPA_Home.this, CartActivity.class);
                startActivity(intent);
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MCPA_Home.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close MCPA_Home activity
            }
        });

        Button promotionsButton = findViewById(R.id.promotionsButton);
        promotionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MCPA_Home.this, PromotionsActivity.class);
                startActivity(intent);
            }
        });

        Button storeLocatorButton = findViewById(R.id.storeLocatorButton);
        storeLocatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MCPA_Home.this, StoreLocatorActivity.class);
                startActivity(intent);
            }
        });
    }
}