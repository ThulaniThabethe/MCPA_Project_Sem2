package com.example.mcpasem2projwoolworths;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Initialize UI components
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        // 2. Initialize Volley's RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // 3. Set the click listener for the register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input and trim whitespace
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // 4. Validate user input
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Registration attempt failed: empty fields.");
                } else {
                    // Proceed with registration if validation passes
                    registerUser(firstName, lastName, email, password);
                }
            }
        });
    }

    /**
     * Sends a POST request to the local server's registration API.
     */
    private void registerUser(String firstName, String lastName, String email, String password) {
        // NOTE: Use your computer's local network IP address, not 127.0.0.1, to connect from the emulator.
        String url = "http://192.168.0.188/MCPA/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                // On successful registration
                                String message = jsonResponse.getString("message");
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Registration successful: " + message);

                                // Optional: Navigate to another activity
                                // Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                // startActivity(intent);
                                // finish();
                            } else {
                                // On server-side failure (e.g., email already exists)
                                String errorMessage = jsonResponse.getString("message");
                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Server-side registration failed: " + errorMessage);
                            }
                        } catch (Exception e) {
                            // On JSON parsing error
                            Toast.makeText(RegisterActivity.this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // On network connection error
                        String errorMessage = "Network error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error");
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Volley network error: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}