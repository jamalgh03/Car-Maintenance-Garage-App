package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditRequest extends AppCompatActivity {
    Spinner requestSpinner;
    Spinner operationsSpinner;
    Button submit;
    ImageButton homeButton;
    private RequestQueue queue;
    private ArrayList<String> requestsList = new ArrayList<>();
    private ArrayList<String> serviceList = new ArrayList<>();
    private ArrayList<String> serviceIdList = new ArrayList<>(); // Store service IDs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        requestSpinner = findViewById(R.id.requsetSpinner);
        operationsSpinner = findViewById(R.id.operationsSpinner);
        submit = findViewById(R.id.submit);
        homeButton = findViewById(R.id.homeButton);

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = getJsonObjectRequest();
        queue.add(request);
        fetchDataAndPopulateSpinners();
        homeButton.setOnClickListener(v -> startActivity(new Intent(EditRequest.this, AdminActivity.class)));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected request and service
                String selectedRequest = requestSpinner.getSelectedItem().toString();
                String selectedService = operationsSpinner.getSelectedItem().toString();

                // Extract request ID from the selected request string
                String requestId = selectedRequest.split(",")[0].replace("ID: ", "").trim();

                // Find the service ID for the selected service
                int serviceIndex = serviceList.indexOf(selectedService);
                if (serviceIndex != -1) {
                    String newServiceId = serviceIdList.get(serviceIndex);

                    // Send request to update the service
                    updateRequestService(requestId, newServiceId);
                } else {
                    Toast.makeText(EditRequest.this, "Invalid service selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JsonObjectRequest getJsonObjectRequest() {
        String url = "http://10.10.10.22/api/getallrequests_json.php"; // Use correct IP for emulator

        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                parseResponse(response);
                            } else {
                                Toast.makeText(EditRequest.this, "Empty response from server", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("JSON_ERROR", "JSONException: " + e.getMessage());
                            Toast.makeText(EditRequest.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Status Code: " + error.networkResponse.statusCode;
                        } else {
                            errorMessage += error.getMessage();
                        }
                        Toast.makeText(EditRequest.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", error.toString());
                    }
                });
    }

    private void parseResponse(JSONObject response) throws JSONException {
        JSONArray allreqArray = response.getJSONArray("allreq");
        JSONArray allreq2Array = response.getJSONArray("allreq2");
        JSONArray allservices = response.getJSONArray("allservices");

        // Clear existing data
        requestsList.clear();
        serviceList.clear();
        serviceIdList.clear();

        // Create a map of service IDs to service names
        Map<String, String> serviceMap = new HashMap<>();
        for (int i = 0; i < allservices.length(); i++) {
            JSONObject row = allservices.getJSONObject(i);
            String id = row.optString("id", "N/A");
            String service_name = row.optString("service_name", "N/A");
            serviceMap.put(id, service_name); // Map service ID to service name
        }

        // Parse allreqArray
        for (int i = 0; i < allreqArray.length(); i++) {
            JSONObject row = allreqArray.getJSONObject(i);
            String id = row.optString("id", "N/A");
            String car_id = row.optString("car_id", "N/A");
            String service_id = row.optString("service_id", "N/A");

            // Get the service name from the serviceMap
            String service_name = serviceMap.get(service_id);
            if (service_name == null) {
                service_name = "Unknown Service"; // Fallback if service ID is not found
            }

            // Create the display string with the service name
            String displayString = "ID: " + id + ", Car ID: " + car_id + ", Service: " + service_name;
            requestsList.add(displayString);
        }

        // Parse allreq2Array
        for (int i = 0; i < allreq2Array.length(); i++) {
            JSONObject row = allreq2Array.getJSONObject(i);
            String id = row.optString("id", "N/A");
            String car_id = row.optString("car_id", "N/A");
            String service_id = row.optString("service_id", "N/A");

            // Get the service name from the serviceMap
            String service_name = serviceMap.get(service_id);
            if (service_name == null) {
                service_name = "Unknown Service"; // Fallback if service ID is not found
            }

            // Create the display string with the service name
            String displayString = "ID: " + id + ", Car ID: " + car_id + ", Service: " + service_name;
            requestsList.add(displayString);
        }

        // Parse allservices for the operationsSpinner
        for (int i = 0; i < allservices.length(); i++) {
            JSONObject row = allservices.getJSONObject(i);
            String id = row.optString("id", "N/A");
            String service_name = row.optString("service_name", "N/A");
            serviceList.add(service_name);
            serviceIdList.add(id); // Store service IDs
        }

        // Set adapters for Spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditRequest.this, android.R.layout.simple_spinner_item, requestsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(EditRequest.this, android.R.layout.simple_spinner_item, serviceList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operationsSpinner.setAdapter(adapter2);
    }

    private void updateRequestService(String requestId, String newServiceId) {
        String url = "http://10.10.10.22/api/update_request_service.php";

        // Create a request body with the request ID and new service ID
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Try to parse the response as JSON
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            Toast.makeText(EditRequest.this, message, Toast.LENGTH_SHORT).show();

                            // Refresh the Spinner data after a successful update
                            if (status.equals("success")) {
                                fetchDataAndPopulateSpinners();
                            }
                        } catch (JSONException e) {
                            // If parsing fails, log the raw response
                            Log.e("JSON_ERROR", "Invalid JSON response: " + response);
                            Toast.makeText(EditRequest.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Status Code: " + error.networkResponse.statusCode;
                        } else {
                            errorMessage += error.getMessage();
                        }
                        Toast.makeText(EditRequest.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Add POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("request_id", requestId);
                params.put("new_service_id", newServiceId);
                return params;
            }
        };

        queue.add(request);
    }
    private void fetchDataAndPopulateSpinners() {
        JsonObjectRequest request = getJsonObjectRequest();
        queue.add(request);
    }
    private void fetchCarName(String carId) {
        String url = "http://10.10.10.22/api/get_car_name.php?car_id=" + carId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("car_name")) {
                                String carName = jsonResponse.getString("car_name");
                                Log.d("CarName", "Car Name: " + carName);
                                // Use the car name as needed
                            } else if (jsonResponse.has("error")) {
                                String error = jsonResponse.getString("error");
                                Log.e("CarNameError", error);
                            }
                        } catch (JSONException e) {
                            Log.e("JSON_ERROR", "Invalid JSON response: " + response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: ";
                        if (error.networkResponse != null) {
                            errorMessage += "Status Code: " + error.networkResponse.statusCode;
                        } else {
                            errorMessage += error.getMessage();
                        }
                        Log.e("VolleyError", errorMessage);
                    }
                });

        queue.add(request);
    }
}
