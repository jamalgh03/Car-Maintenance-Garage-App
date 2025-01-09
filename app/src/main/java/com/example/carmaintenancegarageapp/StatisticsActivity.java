package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class StatisticsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView registeredCarsCount;
    private TextView activeServicesCount;
    private TextView pendingRequestsCount;
    private TextView registeredUsersCount;
    private TextView totalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize all TextViews
        registeredCarsCount = findViewById(R.id.registeredCarsCount);
        activeServicesCount = findViewById(R.id.activeServicesCount);
        pendingRequestsCount = findViewById(R.id.pendingRequestsCount);
        registeredUsersCount = findViewById(R.id.registeredUsersCount);
        totalRevenue = findViewById(R.id.totalRevenue);

        fetchStatistics();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.export_report) {
            Toast.makeText(this, "Exporting report...", Toast.LENGTH_SHORT).show();
            exportReport(); // Execute export directly without requesting permissions
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exportReport() {
        // Get the app-specific directory for documents
        String pdfDirPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/CarMaintenanceReports/";
        String fileName = "StatisticsReport_" + System.currentTimeMillis() + ".pdf";

        // Create the directory if it doesn't exist
        File directory = new File(pdfDirPath);
        if (!directory.exists()) {
            boolean isCreated = directory.mkdirs();
            if (!isCreated) {
                Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        try {
            // Create PDF file
            PdfWriter writer = new PdfWriter(pdfDirPath + fileName);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add report title
            document.add(new Paragraph("Car Maintenance Garage Statistics")
                    .setFontSize(18)
                    .setBold()
                    .setMarginBottom(20));

            // Create statistics table
            float[] columnWidths = {200f, 200f};
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

            // Add table headers
            table.addHeaderCell("Statistic");
            table.addHeaderCell("Value");

            // Add data
            table.addCell("Number of Registered Cars");
            table.addCell(registeredCarsCount.getText().toString());

            table.addCell("Number of Active Services");
            table.addCell(activeServicesCount.getText().toString());

            table.addCell("Number of Pending Requests");
            table.addCell(pendingRequestsCount.getText().toString());

            table.addCell("Number of Registered Users");
            table.addCell(registeredUsersCount.getText().toString());

            table.addCell("Total Revenue");
            table.addCell(totalRevenue.getText().toString());

            document.add(table);

            // Close document
            document.close();

            Toast.makeText(this, "Report exported successfully to: " + pdfDirPath + fileName, Toast.LENGTH_LONG).show();

            // Share the report
            shareReport(pdfDirPath + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to export report", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareReport(String filePath) {
        File file = new File(filePath);
        if(file.exists()){
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Car Maintenance Garage Report");
            intent.putExtra(Intent.EXTRA_TEXT, "Please find the attached statistics report.");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Share Report"));
        } else {
            Toast.makeText(this, "Report not found for sharing", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchStatistics() {
        String url = "http://192.168.0.100/api/getStatistics.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                // Update cars count
                                registeredCarsCount.setText(String.valueOf(response.getInt("car_count")));

                                // Update services count
                                activeServicesCount.setText(String.valueOf(response.getInt("service_count")));

                                // Update pending requests
                                pendingRequestsCount.setText(String.valueOf(response.getInt("pending_count")));

                                // Update users count
                                registeredUsersCount.setText(String.valueOf(response.getInt("user_count")));

                                // Update total revenue
                                String revenue = "$" + response.getInt("total_revenue");
                                totalRevenue.setText(revenue);
                            } else {
                                Toast.makeText(StatisticsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StatisticsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API_ERROR", "Error: " + error.getMessage());
                        Toast.makeText(StatisticsActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add retry policy to handle timeouts
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000, // 30 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh statistics when activity resumes
        fetchStatistics();
    }
}
