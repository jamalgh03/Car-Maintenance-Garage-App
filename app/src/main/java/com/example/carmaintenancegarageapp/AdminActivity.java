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

        if (savedInstanceState != null) {
            // Restore data
            try {
                String rowsArrayString = savedInstanceState .getString("rowsArray");
                if (rowsArrayString != null) {
                    rowsArray = new JSONArray(rowsArrayString);
                    total_cars_num.setText(savedInstanceState.getString("totalCars"));
                    ready_cars_num.setText(savedInstanceState.getString("readyCars"));
                    work_cars_num.setText(savedInstanceState.getString("workCars"));
                    pending_cars_num.setText(savedInstanceState.getString("pendingCars"));
                }
                ArrayList<String> adapterData = savedInstanceState.getStringArrayList("adapterData");
                if (adapterData != null) {
                    adapter = new ArrayAdapter<>(this, R.layout.list_item_background, R.id.textView, adapterData);
                    lst.setAdapter(adapter);
                }
                int filterSelection = savedInstanceState.getInt("filterSelection");
                filterSpinner.setSelection(filterSelection);
                String searchQuery = savedInstanceState.getString("searchQuery");
                search_edit_text.setText(searchQuery);
                applyFilters(filterSelection, searchQuery);
            } catch (JSONException e) {
                Log.e("Error", "Error parsing JSON: " + e.getMessage());
            }
        } else {
            JsonObjectRequest request = getJsonObjectRequest();
            queue.add(request);
        }

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

        // Set up button click listeners
        add_request.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AddRequestAdmin.class)));
        edit_request_admin.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, EditRequest.class)));
        add_car_admin.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AddAdmin.class)));
        notifications.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, Notfication.class)));
        stats.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, StatisticsActivity.class)));
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the rowsArray as a string
        if (rowsArray != null) {
            outState.putString("rowsArray", rowsArray.toString());
        }
        // Save the adapter data
        if (adapter != null && adapter.getCount() > 0) {
            ArrayList<String> adapterData = new ArrayList<>();
            for (int i = 0; i < adapter.getCount(); i++) {
                adapterData.add(adapter.getItem(i));
            }
            outState.putStringArrayList("adapterData", adapterData);
        }
        // Save the filter spinner selection
        outState.putInt("filterSelection", filterSpinner.getSelectedItemPosition());
        // Save the search query
        outState.putString("searchQuery", search_edit_text.getText().toString());
        // Save the counts
        outState.putString("totalCars", total_cars_num.getText().toString());
        outState.putString("readyCars", ready_cars_num.getText().toString());
        outState.putString("workCars", work_cars_num.getText().toString());
        outState.putString("pendingCars", pending_cars_num.getText().toString());
    }
    private @NonNull JsonObjectRequest getJsonObjectRequest() {
        String url = "http://172.26.32.1/api/requests_json.php";
        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String totalStr = response.getString("total");
                            String readyStr = response.getString("ready");
                            String pendingStr = response.getString("pending");
                            String workStr = response.getString("work");

                            total_cars_num.setText(totalStr);
                            ready_cars_num.setText(readyStr);
                            pending_cars_num.setText(pendingStr);
                            work_cars_num.setText(workStr);

                            rowsArray = response.getJSONArray("rows"); // Populate rowsArray
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
    }

    private String formatCarInfo(JSONObject row) throws JSONException {
        String requestId = row.getString("request_id");
        String carNumber = row.getString("car_number");
        String serviceName = row.getString("service_name");
        String email = row.getString("email");
        String status = row.getString("status");

        return "Request ID: " + requestId + " Car Number: " + carNumber + " Service: " + serviceName + " Email: " + email + " Status: " + status;
    }
    private void applyFilters(int filterSelection, String searchQuery) {
        if (!searchQuery.isEmpty()) {
            filterData(searchQuery);
        } else {
            String selectedFilter = filterSpinner.getItemAtPosition(filterSelection).toString();
            filterData(selectedFilter);
        }
    }
    private void filterData(String query) {
        ArrayList<String> filteredCarRequests = new ArrayList<>();

        if (rowsArray != null) {
            for (int i = 0; i < rowsArray.length(); i++) {
                try {
                    JSONObject row = rowsArray.getJSONObject(i);
                    String requestId = row.getString("request_id");
                    String carNumber = row.getString("car_number");
                    String serviceName = row.getString("service_name");
                    String email = row.getString("email");
                    String status = row.getString("status");

                    // Check if the query matches any field
                    if (requestId.toLowerCase().contains(query.toLowerCase()) ||
                            carNumber.toLowerCase().contains(query.toLowerCase()) ||
                            serviceName.toLowerCase().contains(query.toLowerCase()) ||
                            email.toLowerCase().contains(query.toLowerCase()) ||
                            status.toLowerCase().contains(query.toLowerCase())) {
                        filteredCarRequests.add(formatCarInfo(row));
                    }
                } catch (JSONException e) {
                    Log.d("Error", "JSON Exception: " + e.toString());
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
                    Log.d("Error", "JSON Exception: " + e.toString());
                }
            }
        }

        // Update the adapter with all rows
        adapter.clear();
        adapter.addAll(carRequests);
        adapter.notifyDataSetChanged();
    }
    private void showMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());

        String selectedItem = adapter.getItem(position);
        String currentStatus = extractStatus(selectedItem);

        Log.d("SelectedItem", selectedItem);
        Log.d("CurrentStatus", currentStatus);

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
                Log.d("Error", "Unknown status: " + currentStatus);
                break;
        }

        popupMenu.setOnMenuItemClickListener(item -> {
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
        });

        popupMenu.show();
    }
    private void updateStatus(String item, String newStatus) {
        if (rowsArray == null) {
            Log.d("Error", "rowsArray is null");
            Toast.makeText(this, "Data not loaded. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Extract the request ID from the item string
            String[] parts = item.split(" ");
            String requestId = parts[2]; // "Request ID: 1 Car Number: ..." → parts[2] = "1"

            // Find the item in the rowsArray and update its status
            for (int i = 0; i < rowsArray.length(); i++) {
                JSONObject row = rowsArray.getJSONObject(i);
                String id = row.getString("request_id"); // Use "request_id" instead of "id"

                if (requestId.equals(id)) {
                    // Update the status in the JSON object
                    row.put("status", newStatus);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();

                    // Update the status on the server
                    updateStatusOnServer(id, newStatus);

                    // Show a toast message
                    Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } catch (JSONException e) {
            Log.d("Error", "JSON Exception: " + e.toString());
            Toast.makeText(this, "Error updating status", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteItem(String item) {
        try {
            // Extract the request ID from the item string
            String[] parts = item.split(" ");
            String requestId = parts[2]; // "Request ID: 1 Car Number: ..." → parts[2] = "1"

            // Find the item in the rowsArray and remove it
            for (int i = 0; i < rowsArray.length(); i++) {
                JSONObject row = rowsArray.getJSONObject(i);
                String id = row.getString("request_id"); // Use "request_id" instead of "id"

                if (requestId.equals(id)) {
                    rowsArray.remove(i);
                    adapter.notifyDataSetChanged();
                    deleteItemOnServer(id);

                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } catch (JSONException e) {
            Log.d("Error", "JSON Exception: " + e.toString());
            Toast.makeText(this, "Error deleting item", Toast.LENGTH_SHORT).show();
        }
    }
    private String extractStatus(String item) {
        String[] parts = item.split("Status: ");

        if (parts.length > 1) {
            return parts[1].trim();
        }

        return "";
    }
    private void updateStatusOnServer(String id, String newStatus) {
        String url = "http://172.26.32.1/api/update_status.php"; // Replace with your server URL

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

        queue.add(request);
    }
    private void deleteItemOnServer(String id) {
        String url = "http://172.26.32.1/api/delete_request_json.php?request_id=" + id; // Replace with your server URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Check if the deletion was successful
                            boolean success = response.getBoolean("success");
                            String message = response.optString("message", "No message provided");

                            if (success) {
                                Toast.makeText(AdminActivity.this, message, Toast.LENGTH_SHORT).show();
                                refreshData(); // Refresh the data after deletion
                            } else {
                                Toast.makeText(AdminActivity.this, "Failed to delete item: " + message, Toast.LENGTH_SHORT).show();
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


