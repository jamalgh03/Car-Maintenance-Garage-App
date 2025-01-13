package com.example.carmaintenancegarageapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CaptionedImagesAdapter adapter;
    private CarAdapter adapter1 ;
    private String url = "http://172.19.40.34/api/addservice.php";
    private String url1 = "http://172.19.40.34/api/getcars.php";
    private static final String PREFS_NAME = "MyPrefs";

    private Handler handler = new Handler();
    private int currentPosition = 0;
    private ImageButton menuButton , AddButton , listButton , homeButton;
    private ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Horizontal orientation
        ClickAction();
        fetchServiceData();
        startAutoScroll();

        listView = findViewById(R.id.listView);
        fetchCarData(); // fetch car data to populate ListView
    }

    private void fetchServiceData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<ServiceCar> serviceCars = new ArrayList<>();
                            Log.d("FetchServiceData", "Response received: " + response.toString());  // عرض الرد الكامل

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject service = response.getJSONObject(i);
                                String serviceName = service.getString("service_name");
                                String pictureUrl = service.getString("service_picture");
                                Log.d("ServiceData", "Service Name: " + serviceName + ", Picture URL: " + pictureUrl); // عرض البيانات

                                serviceCars.add(new ServiceCar(serviceName, pictureUrl));
                            }

                            adapter = new CaptionedImagesAdapter(serviceCars);
                            recyclerView.setAdapter(adapter);
                            Log.d("Adapter", "Adapter set with " + serviceCars.size() + " items");

                        } catch (JSONException e) {
                            Log.e("FetchServiceData", "JSON parsing error: " + e.getMessage()); // عرض الخطأ
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("FetchServiceData", "Volley error: " + error.getMessage()); // عرض خطأ الاتصال
                        Toast.makeText(MainActivity.this, "Connection error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void fetchCarData() {
        // استرجاع البريد الإلكتروني المخزن في SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("user_id", "");

        if (!userEmail.isEmpty()) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url1 + "?email=" + userEmail,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                List<Car> carList = new ArrayList<>();
                                Log.d("FetchCarData", "Response received: " + response.toString());

                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject carObject = response.getJSONObject(i);
                                    String carName = carObject.getString("car_name");
                                    String carModel = carObject.getString("module");
                                    String carNumber = carObject.getString("car_number");

                                    Log.d("CarData", "Car Name: " + carName + ", Model: " + carModel + ", Number: " + carNumber + ", Email: " + userEmail);

                                    carList.add(new Car(carName, carModel, carNumber));
                                }

                                adapter1 = new CarAdapter(MainActivity.this, carList);
                                listView.setAdapter(adapter1);

                                Log.d("Adapter", "Adapter set with " + carList.size() + " items");

                            } catch (JSONException e) {
                                Log.e("FetchCarData", "JSON parsing error: " + e.getMessage());
                                e.printStackTrace();
                                Log.e("FetchCarData", "Error loading data: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("FetchCarData", "Volley error: " + error.getMessage());
                            Toast.makeText(MainActivity.this, "Connection error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            Volley.newRequestQueue(this).add(jsonArrayRequest);
        } else {
            Toast.makeText(MainActivity.this, "Email not found!", Toast.LENGTH_SHORT).show();
        }
    }


    public void ClickAction(){
        menuButton = findViewById(R.id.menuButton);
        AddButton = findViewById(R.id.AddButton);
        listButton = findViewById(R.id.listButton);
        homeButton = findViewById(R.id.homeButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(MainActivity.this, MoreOptions.class);
                startActivity(profile);
            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(MainActivity.this, AddCar.class);
                startActivity(add);
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent request = new Intent(MainActivity.this, AddRequest.class);
                startActivity(request);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void startAutoScroll() {
        Runnable autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int itemCount = layoutManager.getItemCount();
                        if (itemCount > 0) {
                            currentPosition = (currentPosition + 1) % itemCount;
                            layoutManager.smoothScrollToPosition(recyclerView, null, currentPosition);
                        }
                    }
                }
                handler.postDelayed(this, 3000);
            }
        };

        handler.post(autoScrollRunnable);
    }
}
