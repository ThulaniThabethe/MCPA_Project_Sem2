package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CheckoutActivity extends AppCompatActivity {

    private TextView paymentMethodTextView;
    private Button editCardButton;
    private Button placeOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        paymentMethodTextView = findViewById(R.id.paymentMethodTextView);
        editCardButton = findViewById(R.id.editCardButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        editCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, EditCardActivity.class);
                startActivity(intent);
            }
        });

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mock checkout logic
                Toast.makeText(CheckoutActivity.this, "Order Placed!", Toast.LENGTH_SHORT).show();
                finish(); // Close checkout activity
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update payment method display if needed (e.g., after returning from EditCardActivity)
        // For now, it's a static placeholder.
        paymentMethodTextView.setText("Credit Card (**** **** **** 1234)");
    }
}