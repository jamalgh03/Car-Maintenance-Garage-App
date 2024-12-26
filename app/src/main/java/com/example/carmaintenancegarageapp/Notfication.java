package com.example.carmaintenancegarageapp;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Notfication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notfication);

        // Sample notifications
        String[] notifications = new String[] {
                "Notification 1",
                "Notification 2",
                "Notification 3",
                "Notification 4",
                "Notification 5"
        };

        RecyclerView recyclerView = findViewById(R.id.notificationsRecyclerView);

        // Initialize and set the adapter with data
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);
    }
}
