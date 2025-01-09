package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.EditText;

import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    ImageButton add_request, edit_request_admin, add_car_admin, notifications, stats, searchImg;
    TextView total_cars_num, ready_cars_num, work_cars_num, pending_cars_num;
    Spinner filterSpinner;
    EditText search_edit_text;
    ListView lst;
    private RequestQueue queue;
    private JSONArray rowsArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        search_edit_text = findViewById(R.id.searchEditText);
        searchImg = findViewById(R.id.searchButton);
        add_request = findViewById(R.id.addrequestadmin);
        edit_request_admin = findViewById(R.id.editrequestadmin);
        add_car_admin = findViewById(R.id.addcaradmin);
        notifications = findViewById(R.id.moreoptions);
        stats = findViewById(R.id.stats);
        total_cars_num = findViewById(R.id.totalcarsnum);
        ready_cars_num = findViewById(R.id.totalreadycarsnum);
        work_cars_num = findViewById(R.id.totalworkcarsnum);
        pending_cars_num = findViewById(R.id.totalpendingcarsnum);
        filterSpinner = findViewById(R.id.filterSpinner);
        lst = findViewById(R.id.listView);

        queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = getJsonObjectRequest();
        queue.add(request);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMenu(view, position);
            }
        });
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = search_edit_text.getText().toString().trim();

                if (query.isEmpty()) {
                    resetListView();
                } else {
                    filterSpinner.setSelection(0);
                    filterData(query);
                }
                refreshData();

            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.filter_options,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adapter == null) {
                    Log.e("Error", "Adapter is null");
                    return;
                }

                String selectedFilter = parent.getItemAtPosition(position).toString();
                ArrayList<String> filteredCarRequests = new ArrayList<>();
                if (rowsArray != null) {
                    for (int i = 0; i < rowsArray.length(); i++) {
                        try {
                            JSONObject row = rowsArray.getJSONObject(i);
                            String status = row.getString("status");

                            // Apply the filter
                            switch (selectedFilter) {
                                case "All":
                                    filteredCarRequests.add(formatCarInfo(row));
                                    break;
                                case "Ready":
                                    if (status.equalsIgnoreCase("ready")) {
                                        filteredCarRequests.add(formatCarInfo(row));
                                    }
                                    break;
                                case "Pending":
                                    if (status.equalsIgnoreCase("pending")) {
                                        filteredCarRequests.add(formatCarInfo(row));
                                    }
                                    break;
                                case "Work":
                                    if (status.equalsIgnoreCase("work")) {
                                        filteredCarRequests.add(formatCarInfo(row));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            Log.d("Error", e.toString());
                        }
                    }
                }

                adapter.clear();
                adapter.addAll(filteredCarRequests);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set up button click listeners
        add_request.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AddRequestAdmin.class)));
        edit_request_admin.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, EditRequest.class)));
        add_car_admin.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AddAdmin.class)));
        notifications.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, Notfication.class)));
        stats.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, StatisticsActivity.class)));
    }

    private @NonNull JsonObjectRequest getJsonObjectRequest() {
        String url = "http://10.10.10.22/api/requests_json.php";
        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract counts
                            String totalStr = response.getString("total");
                            String readyStr = response.getString("ready");
                            String pendingStr = response.getString("pending");
                            String workStr = response.getString("work");

                            int all = Integer.parseInt(totalStr);
                            int ready = Integer.parseInt(readyStr);
                            int pending = Integer.parseInt(pendingStr);
                            int work = Integer.parseInt(workStr);

                            total_cars_num.setText(String.valueOf(all));
                            ready_cars_num.setText(String.valueOf(ready));
                            pending_cars_num.setText(String.valueOf(pending));
                            work_cars_num.setText(String.valueOf(work));

                            rowsArray = response.getJSONArray("rows");
                            ArrayList<String> carRequests = new ArrayList<>();

                            for (int i = 0; i < rowsArray.length(); i++) {
                                JSONObject row = rowsArray.getJSONObject(i);
                                carRequests.add(formatCarInfo(row));
                            }

                            adapter = new ArrayAdapter<>(
                                    AdminActivity.this,
                                    R.layout.list_item_background,
                                    R.id.textView,
                                    carRequests
                            );

                            lst.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.d("Error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Error_json", error.toString());
                    }
                });
    }

    private String formatCarInfo(JSONObject row) throws JSONException {
        String id = row.getString("id");
        String userId = row.getString("user_id");
        String carName = row.getString("car_name");
        String status = row.getString("status");

        return "ID: " + id + ", User ID: " + userId + ", Car: " + carName + ", Status: " + status;
    }

    private void filterData(String query) {
        ArrayList<String> filteredCarRequests = new ArrayList<>();

        if (rowsArray != null) {
            for (int i = 0; i < rowsArray.length(); i++) {
                try {
                    JSONObject row = rowsArray.getJSONObject(i);
                    String id = row.getString("id");
                    String userId = row.getString("user_id");
                    String carName = row.getString("car_name");
                    String status = row.getString("status");

                    if (id.toLowerCase().contains(query.toLowerCase()) ||
                        userId.toLowerCase().contains(query.toLowerCase()) ||
                        carName.toLowerCase().contains(query.toLowerCase()) ||
                        status.toLowerCase().contains(query.toLowerCase())) {
                        filteredCarRequests.add(formatCarInfo(row));
                    }
                } catch (JSONException e) {
                    Log.d("Error", e.toString());
                }
            }
        }

        adapter.clear();
        adapter.addAll(filteredCarRequests);
        adapter.notifyDataSetChanged();
    }

    private void resetListView() {
        ArrayList<String> carRequests = new ArrayList<>();

        if (rowsArray != null) {
            for (int i = 0; i < rowsArray.length(); i++) {
                try {
                    JSONObject row = rowsArray.getJSONObject(i);
                    carRequests.add(formatCarInfo(row));
                } catch (JSONException e) {
                    Log.d("Error", e.toString());
                }
            }
        }

        adapter.clear();
        adapter.addAll(carRequests);
        adapter.notifyDataSetChanged();
    }
    private void showMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        String selectedItem = adapter.getItem(position);
        String currentStatus = extractStatus(selectedItem);
        Menu menu = popupMenu.getMenu();
        MenuItem pendingItem = menu.findItem(R.id.menu_pending);
        MenuItem workItem = menu.findItem(R.id.menu_work);
        MenuItem readyItem = menu.findItem(R.id.menu_ready);
        MenuItem deleteItem = menu.findItem(R.id.menu_delete);

        switch (currentStatus) {
            case "pending":
                pendingItem.setEnabled(false);
                workItem.setEnabled(true);
                readyItem.setEnabled(false);
                deleteItem.setEnabled(true);
                break;
            case "work":
                pendingItem.setEnabled(false);
                workItem.setEnabled(false);
                readyItem.setEnabled(true);
                deleteItem.setEnabled(true);
                break;
            case "ready":
                pendingItem.setEnabled(false);
                workItem.setEnabled(false);
                readyItem.setEnabled(false);
                deleteItem.setEnabled(true);
                break;
            default:
                pendingItem.setEnabled(false);
                workItem.setEnabled(false);
                readyItem.setEnabled(false);
                deleteItem.setEnabled(true);
                break;
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_pending) {
                    updateStatus(selectedItem, "pending");
                    return true;
                } else if (itemId == R.id.menu_work) {
                    updateStatus(selectedItem, "work");
                    return true;
                } else if (itemId == R.id.menu_ready) {
                    updateStatus(selectedItem, "ready");
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    deleteItem(selectedItem);
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Set a dismiss listener to handle clicks outside the menu
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // Do nothing or add any cleanup logic if needed
            }
        });

        // Show the popup menu
        popupMenu.show();
    }
    private void updateStatus(String item, String newStatus) {
        for (int i = 0; i < rowsArray.length(); i++) {
            try {
                JSONObject row = rowsArray.getJSONObject(i);
                String id = row.getString("id");
                String userId = row.getString("user_id");
                String carName = row.getString("car_name");
                String status = row.getString("status");

                if (item.equals("ID: " + id + ", User ID: " + userId + ", Car: " + carName + ", Status: " + status)) {
                    row.put("status", newStatus);

                    adapter.notifyDataSetChanged();
                    updateStatusOnServer(id,newStatus);


                    Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    break;
                }
            } catch (JSONException e) {
                Log.d("Error", e.toString());
            }
        }
    }
    private void deleteItem(String item) {
        // Find the item in the rowsArray and remove it
        for (int i = 0; i < rowsArray.length(); i++) {
            try {
                JSONObject row = rowsArray.getJSONObject(i);
                String id = row.getString("id");
                String userId = row.getString("user_id");
                String carName = row.getString("car_name");
                String status = row.getString("status");

                // Check if this is the selected item
                if (item.equals("ID: " + id + ", User ID: " + userId + ", Car: " + carName + ", Status: " + status)) {
                    // Remove the item from the rowsArray
                    rowsArray.remove(i);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();

                    // Optionally, send an API request to delete the item on the server
                     deleteItemOnServer(id);

                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                    break;
                }
            } catch (JSONException e) {
                Log.d("Error", e.toString());
            }
        }
    }
    private String extractStatus(String item) {
        String[] parts = item.split(", ");
        for (String part : parts) {
            if (part.startsWith("Status: ")) {
                return part.substring("Status: ".length());
            }
        }
        return "";
    }
    private void updateStatusOnServer(String id, String newStatus) {
        String url = "http://10.10.10.22/api/update_status.php"; // Replace with your server URL

        // Create a JSON object with the parameters
        JSONObject params = new JSONObject();
        try {
            params.put("id", id);
            params.put("status", newStatus);
        } catch (JSONException e) {
            Log.d("Error", "JSON Exception: " + e.toString());
            return;
        }

        // Create a POST request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(AdminActivity.this, "Status updated successfully", Toast.LENGTH_SHORT).show();
                                refreshData(); // Refresh the data after updating the status
                            } else {
                                String message = response.optString("message", "Failed to update status");
                                Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("Error", "JSON Exception: " + e.toString());
                            Toast.makeText(AdminActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error_json", "Volley Error: " + error.toString());
                        Toast.makeText(AdminActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the queue
        queue.add(request);
    }
    private void deleteItemOnServer(String id) {
        String url = "http://10.10.10.22/api/delete_item.php?id=" + id; // Replace with your server URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(AdminActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                refreshData();
                            } else {
                                String message = response.optString("message", "Failed to delete item");
                                Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.d("Error", "JSON Exception: " + e.toString());
                            Toast.makeText(AdminActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error_json", "Volley Error: " + error.toString());
                        Toast.makeText(AdminActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }
    private void refreshData() {

        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        JsonObjectRequest request = getJsonObjectRequest();
        queue.add(request);
    }
}


