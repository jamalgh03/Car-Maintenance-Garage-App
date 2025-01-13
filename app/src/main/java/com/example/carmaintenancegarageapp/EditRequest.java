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
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
    private ArrayList<String> serviceIdList = new ArrayList<>();

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
                String selectedRequest = requestSpinner.getSelectedItem().toString();
                String selectedService = operationsSpinner.getSelectedItem().toString();
                String requestId = selectedRequest.split(",")[0].replace("ID: ", "").trim();
                int serviceIndex = serviceList.indexOf(selectedService);
                if (serviceIndex != -1) {
                    String newServiceId = serviceIdList.get(serviceIndex);
                    updateRequestService(requestId, newServiceId);
                } else {
                    Toast.makeText(EditRequest.this, "Invalid service selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private JsonObjectRequest getJsonObjectRequest() {
        String url = "http://172.19.40.34/api/getallrequests_json.php";
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
        requestsList.clear();
        serviceList.clear();
        serviceIdList.clear();

        JSONArray allreqArray = response.optJSONArray("allreq");
        JSONArray allservicesArray = response.optJSONArray("allservices");
        JSONArray allcarsArray = response.optJSONArray("allcars");

        if (allreqArray == null || allservicesArray == null || allcarsArray == null) {
            Toast.makeText(this, "Invalid data format from server", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < allreqArray.length(); i++) {
            JSONObject row = allreqArray.getJSONObject(i);
            String requestId = row.optString("request_id", "N/A");
            String carNumber = row.optString("car_number", "N/A");
            String serviceName = row.optString("service_name", "N/A");
            String displayString = "ID: " + requestId + ", Car: " + carNumber + ", Service: " + serviceName;
            requestsList.add(displayString);
        }

        for (int i = 0; i < allservicesArray.length(); i++) {
            JSONObject row = allservicesArray.getJSONObject(i);
            String serviceName = row.optString("service_name", "N/A");
            String servicePicture = row.optString("service_picture", "N/A");
            if (!serviceName.equals("N/A")) {
                serviceList.add(serviceName);
                serviceIdList.add(serviceName);
            }
        }

        for (int i = 0; i < allcarsArray.length(); i++) {
            JSONObject row = allcarsArray.getJSONObject(i);
            String carName = row.optString("car_name", "N/A");
            String carModel = row.optString("car_model", "N/A");
            String logo = row.optString("logo", "N/A");
        }

        setSpinnerAdapters();
    }

    private void setSpinnerAdapters() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, requestsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, serviceList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operationsSpinner.setAdapter(adapter2);
    }

    private void updateRequestService(String requestId, String newServiceName) {
        String url = "http://172.19.40.34/api/update_request_service.php";

        Log.d("RequestParams", "request_id: " + requestId + ", new_service_name: " + newServiceName);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("ServerResponse", response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        Toast.makeText(EditRequest.this, message, Toast.LENGTH_SHORT).show();
                        if (status.equals("success")) {
                            fetchDataAndPopulateSpinners();
                        }
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Invalid JSON response: " + response);
                        Toast.makeText(EditRequest.this, "Invalid server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMessage = "Error: ";
                    if (error.networkResponse != null) {
                        errorMessage += "Status Code: " + error.networkResponse.statusCode;
                    } else {
                        errorMessage += error.getMessage();
                    }
                    Toast.makeText(EditRequest.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("VolleyError", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("request_id", requestId);
                params.put("new_service_name", newServiceName);
                return params;
            }
        };

        queue.add(request);
    }

    private void fetchDataAndPopulateSpinners() {
        JsonObjectRequest request = getJsonObjectRequest();
        queue.add(request);
    }
}