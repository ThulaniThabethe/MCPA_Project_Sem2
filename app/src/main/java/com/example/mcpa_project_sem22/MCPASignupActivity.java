package com.example.mcpa_project_sem22;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MCPASignupActivity extends AppCompatActivity {

    private static final String TAG = "MCPASignupActivity";

    // Declare UI components based on your XML file
    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Button clearButton, createAccountButton;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this Java file to your XML layout file
        setContentView(R.layout.activity_mcpa_signup);

        // Initialize UI components using their IDs from the XML
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        clearButton = findViewById(R.id.clear_button);
        createAccountButton = findViewById(R.id.create_account_button);

        // Initialize Volley's RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Set the click listener for the create account button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input and trim whitespace
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();

                // 4. Validate user input
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(MCPASignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Registration attempt failed: empty fields.");
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(MCPASignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Registration attempt failed: passwords do not match.");
                } else {
                    // Proceed with registration if validation passes
                    registerUser(email, password);
                }
            }
        });

        // Set the click listener for the clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailInput.setText("");
                passwordInput.setText("");
                confirmPasswordInput.setText("");
            }
        });
    }

    /**
     * Sends a POST request to the local server's registration API.
     */
    private void registerUser(String email, String password) {
        // NOTE: Use your computer's local network IP address, not 127.0.0.1.
        String url = "http://172.20.10.6/MCPA/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                // On successful registration
                                String message = jsonResponse.getString("message");
                                Toast.makeText(MCPASignupActivity.this, message, Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Registration successful: " + message);

                                // Optional: Navigate back to the login screen
                                finish();
                            } else {
                                // On server-side failure (e.g., email already exists)
                                String errorMessage = jsonResponse.getString("message");
                                Toast.makeText(MCPASignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Server-side registration failed: " + errorMessage);
                            }
                        } catch (JSONException e) {
                            // On JSON parsing error
                            Toast.makeText(MCPASignupActivity.this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Network error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error");
                        Toast.makeText(MCPASignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Volley network error: " + error.toString());

                        // Add this to see the raw response from the server
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                Log.e(TAG, "Server response: " + responseBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // The keys here must match the $_POST keys in your register.php script
                params.put("email", email);
                params.put("password", password);
                // The PHP script also needs 'first_name' and 'last_name'.
                // You'll need to add them to your XML or make them optional in your PHP script.
                // For now, let's assume your PHP script is updated to just use email and password.
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}