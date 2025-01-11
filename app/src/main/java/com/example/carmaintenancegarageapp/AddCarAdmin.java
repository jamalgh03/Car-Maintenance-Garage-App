package com.example.carmaintenancegarageapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddCarAdmin extends AppCompatActivity {

    private Spinner userSpinner;
    private Spinner carNameSpinner;
    private ListView modelListView;
    private EditText carNumberEditText;

    // PHP URLs
    private static final String FETCH_USERS_URL = "http://192.168.56.1/fetch_users.php";
    private static final String FETCH_CARS_URL = "http://192.168.56.1/fetch_cars.php";
    private static final String INSERT_CAR_URL = "http://192.168.56.1/insert_car_admin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_admin);


        userSpinner = findViewById(R.id.userSpinner);
        carNameSpinner = findViewById(R.id.optionSpinner);
        modelListView = findViewById(R.id.itemListView);
        carNumberEditText = findViewById(R.id.carNumberEditText);


        loadUserEmails();
        loadCarNames();


        carNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCarName = parent.getItemAtPosition(position).toString();
                loadCarModels(selectedCarName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        modelListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedModel = parent.getItemAtPosition(position).toString();
            insertCarForUser(selectedModel);
        });
    }


    private void loadUserEmails() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, FETCH_USERS_URL, response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                ArrayList<String> userEmails = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    userEmails.add(jsonArray.getString(i));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userEmails);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userSpinner.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading user emails", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, "Error fetching user emails", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    private void loadCarNames() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, FETCH_CARS_URL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                ArrayList<String> carNames = new ArrayList<>();

                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    carNames.add(keys.next());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                carNameSpinner.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading car names", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, "Error fetching car names", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }


    private void loadCarModels(String carName) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, FETCH_CARS_URL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                ArrayList<String> carModels = new ArrayList<>();

                for (int i = 0; i < jsonObject.getJSONArray(carName).length(); i++) {
                    carModels.add(jsonObject.getJSONArray(carName).getString(i));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carModels);
                modelListView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading car models", Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(this, "Error fetching car models", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    private void insertCarForUser(String selectedModel) {
        String carNumber = carNumberEditText.getText().toString().trim();
        String carName = carNameSpinner.getSelectedItem().toString();
        String selectedUserEmail = userSpinner.getSelectedItem().toString();

        if (carNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a car number.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_CAR_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if (status.equals("success")) {
                            Toast.makeText(this, "Car added successfully for " + selectedUserEmail, Toast.LENGTH_SHORT).show();
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to add car", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("car_name", carName);
                params.put("module", selectedModel);
                params.put("car_number", carNumber);
                params.put("email", selectedUserEmail);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
