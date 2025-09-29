package com.example.mcpa_project_sem22;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditCardActivity extends AppCompatActivity {

    private EditText cardNumberEditText;
    private EditText expiryDateEditText;
    private EditText cvvEditText;
    private EditText cardholderNameEditText;
    private Button saveCardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        cardholderNameEditText = findViewById(R.id.cardholderNameEditText);
        saveCardButton = findViewById(R.id.saveCardButton);

        saveCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCardDetails();
            }
        });
    }

    private void saveCardDetails() {
        String cardNumber = cardNumberEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();
        String cvv = cvvEditText.getText().toString();
        String cardholderName = cardholderNameEditText.getText().toString();

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty() || cardholderName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you would typically save the card details to a database or a secure storage.
        // For this mock, we'll just show a toast message.
        Toast.makeText(this, "Card details saved!", Toast.LENGTH_SHORT).show();
        finish(); // Close the activity after saving
    }
}