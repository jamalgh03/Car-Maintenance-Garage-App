package com.example.carmaintenancegarageapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notfication extends AppCompatActivity {

    private static final String TAG = "Notfication";
    private static final String REQUEST_URL = "http://172.19.40.34/api/readyRequest.php?email=jane@example.com"; // استبدل بالرابط الصحيح والبريد الإلكتروني
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<ShowNotfication> notificationList;
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notfication);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // تهيئة القائمة والإعدادات
        recyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList, this); // تمرير قائمة النصوص فقط

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // جلب البيانات من قاعدة البيانات
        fetchNotifications();
    }

    private void fetchNotifications() {
        String userEmail = sharedPreferences.getString("user_id", "");

        if (!userEmail.isEmpty()) {
            String requestUrl = "http://172.19.40.34/api/readyRequest.php?email=" + userEmail;

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    requestUrl,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    String carName = obj.getString("car_name");
                                    String carNumber = obj.getString("car_number");
                                    String status = obj.getString("status");
                                    String msg = "Car name is " + carName + ", car number is " + carNumber + ", Status: " + status + ". You can receive the car.";

                                    ShowNotfication notification = new ShowNotfication(carName, carNumber, status, msg);
                                    notificationList.add(notification);
                                }

                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing Error: " + e.getMessage());
                                Toast.makeText(Notfication.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Volley Error: " + error.getMessage());
                            Toast.makeText(Notfication.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            requestQueue.add(jsonArrayRequest);
        } else {
            Toast.makeText(this, "Email not found!", Toast.LENGTH_SHORT).show();
        }
    }

}
