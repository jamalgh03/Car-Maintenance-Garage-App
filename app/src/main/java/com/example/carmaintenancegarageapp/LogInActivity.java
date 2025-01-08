package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button continueButton, signUpButton;
    private ImageButton openedEyeButton, closedEyeButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        continueButton = findViewById(R.id.continueButton);
        signUpButton = findViewById(R.id.signUpButton);
        openedEyeButton = findViewById(R.id.openedEyeButton);
        closedEyeButton = findViewById(R.id.closedEyeButton);

        // Default state: password hidden
        closedEyeButton.setVisibility(View.VISIBLE);
        openedEyeButton.setVisibility(View.GONE);

        // Set up button listeners
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to SignUpActivity
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        closedEyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        openedEyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setSelection(passwordEditText.getText().length()); // Keep cursor at the end
            closedEyeButton.setVisibility(View.VISIBLE);
            openedEyeButton.setVisibility(View.GONE);
            isPasswordVisible = false;
        } else {
            // Show password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            passwordEditText.setSelection(passwordEditText.getText().length()); // Keep cursor at the end
            closedEyeButton.setVisibility(View.GONE);
            openedEyeButton.setVisibility(View.VISIBLE);
            isPasswordVisible = true;
        }
    }

    private void logIn() {
        // Get user inputs
        String usernameOrEmail = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(usernameOrEmail) || TextUtils.isEmpty(password)) {
            Toast.makeText(LogInActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL for login
        String url = "http://192.168.0.59/CarMaintenance/login.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle server response
                        Log.d("LogInResponse", response);

                        if (response.contains("admin")) {
                            Toast.makeText(LogInActivity.this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (response.contains("user")) {
                            Toast.makeText(LogInActivity.this, "Welcome, User!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LogInActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("LogInError", error.toString());
                        Toast.makeText(LogInActivity.this, "Failed to log in! Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Send data as POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("usernameOrEmail", usernameOrEmail);
                params.put("password", password);
                return params;
            }
        };

        // Add the request to the Volley queue
        queue.add(stringRequest);
    }
}
