package com.example.mcpa_project_sem22;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText passwordEditText, emailEditText;
    private Button signInButton, registerButton, justBrowsingButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signInButton = findViewById(R.id.sign_in_button);
        registerButton = findViewById(R.id.register_button);
        justBrowsingButton = findViewById(R.id.just_browsing_button);

        // Initialize Volley's RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MCPASignupActivity.class);
                startActivity(intent);
            }
        });

        justBrowsingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
                startActivity(intent);
            }
        });

        // You can remove all DatabaseHelper and addPromotion calls if you're not using a local DB.
    }

    private void loginUser(final String email, final String password) {
        String url = "http://172.20.10.6/MCPA/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getBoolean("success")) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String errorMessage = jsonResponse.getString("message");
                                Toast.makeText(MainActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            Toast.makeText(MainActivity.this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley network error: " + error.toString());
                        Toast.makeText(MainActivity.this, "Network Error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}