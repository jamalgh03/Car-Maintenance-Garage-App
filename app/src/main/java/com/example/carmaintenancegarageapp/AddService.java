package com.example.carmaintenancegarageapp;

import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddService extends AppCompatActivity {
    private Button submitButton;
    private EditText serviceNameEditText;
    private static final String UPLOAD_URL = "http://172.19.40.34/api/AddServices.php"; // استبدل بعنوان API الخاص بك

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        // Find the UI elements
        submitButton = findViewById(R.id.submitButton);
        serviceNameEditText = findViewById(R.id.editText);

        // Set click listener to submit data
        submitButton.setOnClickListener(v -> {
            String serviceName = serviceNameEditText.getText().toString().trim();
            if (!serviceName.isEmpty()) {
                addServiceToServer(serviceName);
            } else {
                Toast.makeText(this, "Please enter a service name.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addServiceToServer(String serviceName) {
        // Send data to server using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("service_name", serviceName); // Add the service name
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
