package com.example.carmaintenancegarageapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Notfication extends AppCompatActivity {
    private ImageButton menuButton , AddButton , listButton , homeButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void ClickAction(){
        menuButton = findViewById(R.id.menuButton);
        AddButton = findViewById(R.id.AddButton);
        listButton = findViewById(R.id.listButton);
        homeButton = findViewById(R.id.homeButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Notfication.this, MoreOptions.class);
                startActivity(profile);


            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(Notfication.this, AddCar.class);
                startActivity(add);


            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent request = new Intent(Notfication.this, AddRequest.class);
                startActivity(request);


            }
        }); homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent request = new Intent(Notfication.this, MainActivity.class);
                startActivity(request);
            }
        });
    }
}
