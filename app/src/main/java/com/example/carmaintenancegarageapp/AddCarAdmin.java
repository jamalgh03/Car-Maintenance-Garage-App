package com.example.carmaintenancegarageapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AddCarAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_admin);
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCarAdmin.this, AdminActivity.class);
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
                Intent intent = new Intent(AddCarAdmin.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCarAdmin.this, AddRequestAdmin.class);
                startActivity(intent);
            }
        });

        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCarAdmin.this, AddCarAdmin.class);
                startActivity(intent);
            }
        });

          menuButton.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {
          Intent intent = new Intent(AddCarAdmin.this, MoreOptions.class);
          startActivity(intent);
          }
         });

    }
}