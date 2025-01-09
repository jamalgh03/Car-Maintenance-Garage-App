package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, phoneEditText, emailEditText, passwordEditText;
    private MaterialButton signUpButton;
    private TextView passwordStrengthTextView;
    private ImageButton openedEyeButton, closedEyeButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.SignUpButton);
        openedEyeButton = findViewById(R.id.openedEyeButton);
        closedEyeButton = findViewById(R.id.closedEyeButton);

        // Default state: password hidden
        closedEyeButton.setVisibility(View.VISIBLE);
        openedEyeButton.setVisibility(View.GONE);

        // Set up the eye button listeners
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

        // Set up the sign-up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
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

    private void signUp() {
        // Get user inputs
        String username = usernameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send data to the server
        String url = "http://192.168.0.100/api/register.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response from the server
                        Log.d("SignUpResponse", response);
                        Toast.makeText(SignUpActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();

                        // Redirect to login screen
                        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("SignUpError", error.toString());
                        Toast.makeText(SignUpActivity.this, "Failed to sign up! Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Send data as POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("name", username);
                params.put("phone", phone);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        // Add the request to the Volley queue
        queue.add(stringRequest);
    }
}
