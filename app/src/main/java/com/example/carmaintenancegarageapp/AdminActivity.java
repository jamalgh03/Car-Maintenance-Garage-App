package com.example.carmaintenancegarageapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

    }
}

//هاض للحالات  NOT_STARTED  CANCELLED  FINISHED  IN_PROGRESS
// dialog_update_status

//public class AdminActivity extends AppCompatActivity {
//
//    private void showUpdateStatusDialog(Car car) {
//        // Create dialog
//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_update_status);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        // Initialize views
//        TextView carInfoText = dialog.findViewById(R.id.carInfoText);
//        Button btnNotStarted = dialog.findViewById(R.id.btnNotStarted);
//        Button btnPending = dialog.findViewById(R.id.btnPending);
//        Button btnInProgress = dialog.findViewById(R.id.btnInProgress);
//        Button btnFinished = dialog.findViewById(R.id.btnFinished);
//        Button btnCancelled = dialog.findViewById(R.id.btnCancelled);
//
//        // Set car info
//        carInfoText.setText(car.getModel() + " - " + car.getOwnerName());
//
//        // Set click listeners
//        btnNotStarted.setOnClickListener(v -> {
//            updateCarStatus(car, CarStatus.NOT_STARTED);
//            dialog.dismiss();
//        });
//
//        btnPending.setOnClickListener(v -> {
//            updateCarStatus(car, CarStatus.PENDING);
//            dialog.dismiss();
//        });
//
//        btnInProgress.setOnClickListener(v -> {
//            updateCarStatus(car, CarStatus.IN_PROGRESS);
//            dialog.dismiss();
//        });
//
//        btnFinished.setOnClickListener(v -> {
//            updateCarStatus(car, CarStatus.FINISHED);
//            dialog.dismiss();
//        });
//
//        btnCancelled.setOnClickListener(v -> {
//            updateCarStatus(car, CarStatus.CANCELLED);
//            dialog.dismiss();
//        });
//
//        dialog.show();
//    }
//
//    private void updateCarStatus(Car car, CarStatus newStatus) {
//        // Update status in database
//        car.setStatus(newStatus);
//        databaseReference.child("cars").child(car.getId()).child("status").setValue(newStatus);
//
//        // Show confirmation
//        Toast.makeText(this, "Status updated to: " + newStatus.toString(), Toast.LENGTH_SHORT).show();
//
//        // Refresh list
//        adapter.notifyDataSetChanged();
//
//        // Optional: Send notification to owner
//        String message = "Your car status has been updated to " + newStatus.toString();
//        sendNotificationToOwner(car.getOwnerId(), message);
//    }
//}