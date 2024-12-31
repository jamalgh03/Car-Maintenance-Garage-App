package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddRequestAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_request_admin);
        Button performOperationsButton = findViewById(R.id.performOperationsButton);
        performOperationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRequestAdmin.this, AdminActivity.class); // Replace AnotherAdminActivity with the target activity name
                startActivity(intent);
            }
        });

        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton requestButton = findViewById(R.id.listButton);
        ImageButton carButton = findViewById(R.id.favoritesButton);
         ImageButton menuButton = findViewById(R.id.menuButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRequestAdmin.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRequestAdmin.this, AddRequestAdmin.class);
                startActivity(intent);
            }
        });

        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRequestAdmin.this, AddCarAdmin.class);
                startActivity(intent);
            }
        });

          menuButton.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {
          Intent intent = new Intent(AddRequestAdmin.this, MoreOptions.class);
          startActivity(intent);
          }
         });

    }
}