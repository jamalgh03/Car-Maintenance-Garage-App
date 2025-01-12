package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRequest extends AppCompatActivity {

    private Spinner carSpinner;
    private CheckBox checkboxOilChange, checkboxTireChange, checkboxBatteryCheck, checkboxEngineCheck;
    private Button performOperationsButton;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String FETCH_CARS_URL = "http://192.168.56.1/fetch_user_cars.php";
    private static final String INSERT_REQUEST_URL = "http://192.168.56.1/insert_request.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        carSpinner = findViewById(R.id.carSpinner);
        checkboxOilChange = findViewById(R.id.checkboxOilChange);
        checkboxTireChange = findViewById(R.id.checkboxTireChange);
        checkboxBatteryCheck = findViewById(R.id.checkboxBatteryCheck);
        checkboxEngineCheck = findViewById(R.id.checkboxEngineCheck);
        performOperationsButton = findViewById(R.id.performOperationsButton);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadUserCars();

        performOperationsButton.setOnClickListener(v -> submitRequest());

        initializeNavigationButtons();
    }


    private void loadUserCars() {
        String userEmail = sharedPreferences.getString("email", "");

        String url = FETCH_CARS_URL + "?email=" + userEmail;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                ArrayList<String> carNumbers = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    carNumbers.add(jsonArray.getString(i));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carNumbers);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                carSpinner.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading cars", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, "Error fetching car data", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }


    private void submitRequest() {
        String selectedCar = carSpinner.getSelectedItem().toString();
        String userEmail = sharedPreferences.getString("email", "");

        ArrayList<String> selectedServices = new ArrayList<>();

        if (checkboxOilChange.isChecked()) selectedServices.add("Oil Change");
        if (checkboxTireChange.isChecked()) selectedServices.add("Tire Change");
        if (checkboxBatteryCheck.isChecked()) selectedServices.add("Battery Check");
        if (checkboxEngineCheck.isChecked()) selectedServices.add("Engine Check");

        if (selectedServices.isEmpty()) {
            Toast.makeText(this, "Please select at least one service", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String service : selectedServices) {
            insertServiceRequest(selectedCar, userEmail, service);
        }
    }


    private void insertServiceRequest(String carNumber, String email, String serviceName) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_REQUEST_URL,
                response -> {
                    Toast.makeText(this, "Request submitted for " + serviceName, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddRequest.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                }, error -> Toast.makeText(this, "Failed to submit request", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("car_number", carNumber);
                params.put("email", email);
                params.put("service_name", serviceName);
                return params;
            }
        };

        queue.add(stringRequest);
    }


    private void initializeNavigationButtons() {
        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton requestButton = findViewById(R.id.listButton);
        ImageButton carButton = findViewById(R.id.favoritesButton);
        ImageButton menuButton = findViewById(R.id.menuButton);

        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddRequest.this, MainActivity.class);
            startActivity(intent);
        });

        requestButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddRequest.this, AddRequest.class);
            startActivity(intent);
        });

        carButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddRequest.this, AddCar.class);
            startActivity(intent);
        });

        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddRequest.this, MoreOptions.class);
            startActivity(intent);
        });
    }
}
