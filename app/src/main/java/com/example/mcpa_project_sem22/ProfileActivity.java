package com.example.mcpa_project_sem22;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mcpa_project_sem22.dao.UserDAO;
import com.example.mcpa_project_sem22.models.User;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText editTextFirstName, editTextLastName, editTextEmail, editTextPhoneNumber, editTextDateOfBirth;
    private Button buttonSaveProfile, buttonViewOrderHistory, uploadProfilePictureButton, storeLocatorButton;
    private CircleImageView profileImageView;
    private UserDAO userDAO;
    private User currentUser;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userDAO = new UserDAO(this);

        // Initialize UI components
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);
        buttonViewOrderHistory = findViewById(R.id.viewOrderHistoryButton);
        uploadProfilePictureButton = findViewById(R.id.uploadProfilePictureButton);
        profileImageView = findViewById(R.id.profileImageView);
        storeLocatorButton = findViewById(R.id.storeLocatorButton);

        // Load user data (assuming user ID is passed via Intent or retrieved from SharedPreferences)
        // For now, let\'s assume a dummy user ID or retrieve from a shared preference/session manager
        int userId = 1; // Replace with actual logged-in user ID
        currentUser = userDAO.getUserById(userId);

        if (currentUser != null) {
            populateProfileData(currentUser);
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user not found
        }

        // Set up DatePickerDialog for Date of Birth
        editTextDateOfBirth.setOnClickListener(v -> showDatePickerDialog());

        // Set up Save Profile button click listener
        buttonSaveProfile.setOnClickListener(v -> saveProfile());

        // Set up View Order History button click listener
        buttonViewOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Set click listener for Upload Profile Picture button
        uploadProfilePictureButton.setOnClickListener(v -> openImageChooser());

        // Set click listener for Store Locator button
        storeLocatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, StoreLocatorActivity.class);
            startActivity(intent);
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                if (inputStream != null) {
                    int imageSize = inputStream.available(); // Get image size in bytes
                    inputStream.close();

                    // Define a maximum allowed image size (e.g., 2MB)
                    long MAX_IMAGE_SIZE_BYTES = 2 * 1024 * 1024; // 2 MB

                    if (imageSize > MAX_IMAGE_SIZE_BYTES) {
                        Toast.makeText(this, "Image size too large. Please select an image smaller than 2MB.", Toast.LENGTH_LONG).show();
                    } else {
                        profileImageView.setImageURI(imageUri);
                        Toast.makeText(this, "Profile picture selected!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Failed to read image data.", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error selecting image: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void populateProfileData(User user) {
        editTextFirstName.setText(user.getFirstName());
        editTextLastName.setText(user.getLastName());
        editTextEmail.setText(user.getEmail());
        editTextPhoneNumber.setText(user.getPhoneNumber());
        editTextDateOfBirth.setText(user.getDateOfBirth());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%d-%02d-%02d", selectedYear, (selectedMonth + 1), selectedDay);
                    editTextDateOfBirth.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void saveProfile() {
        if (currentUser != null) {
            currentUser.setFirstName(editTextFirstName.getText().toString());
            currentUser.setLastName(editTextLastName.getText().toString());
            currentUser.setPhoneNumber(editTextPhoneNumber.getText().toString());
            currentUser.setDateOfBirth(editTextDateOfBirth.getText().toString());

            if (userDAO.updateUser(currentUser)) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}