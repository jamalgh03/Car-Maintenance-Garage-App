package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
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

public class AddNewCarManufacturer extends AppCompatActivity {
    EditText carNameEditText;
    EditText carModelEditText;
    Button submit;
    ImageButton home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car_manufacturer);

        carNameEditText = findViewById(R.id.carNumberEditText);
        carModelEditText = findViewById(R.id.carModelEditText);
        submit = findViewById(R.id.submit);
        home = findViewById(R.id.homeButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carName = carNameEditText.getText().toString().trim();
                String carModel = carModelEditText.getText().toString().trim();

                if (carName.isEmpty() || carModel.isEmpty()) {
                    Toast.makeText(AddNewCarManufacturer.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    addNewCar(carName, carModel);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AddNewCarManufacturer.this, AdminActivity.class);
                startActivity(ig);
            }
        });
    }

    private void addNewCar(String carName, String carModel) {
        String url = "http://172.26.32.1/api/addnewcarmanufacturer.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddNewCarManufacturer.this, response, Toast.LENGTH_SHORT).show();

                        carNameEditText.setText("");
                        carModelEditText.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddNewCarManufacturer.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("car_name", carName);
                params.put("car_model", carModel);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}