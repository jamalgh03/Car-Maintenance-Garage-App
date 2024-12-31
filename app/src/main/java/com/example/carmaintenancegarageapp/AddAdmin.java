package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddAdmin extends AppCompatActivity {

    ImageButton add_car_admin;
    ImageButton add_new_car_manufactor;
    ImageButton add_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        add_new_car_manufactor = findViewById(R.id.addnewcarmanufactoradmin);
        add_car_admin = findViewById(R.id.addcar);
        add_service = findViewById(R.id.addservice);
        add_car_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AddAdmin.this, AddCarAdmin.class);
                startActivity(ig);

            }
        });
        add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AddAdmin.this, AddService.class);
                startActivity(ig);

            }
        });
        add_new_car_manufactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AddAdmin.this, AddNewCarManufacturer.class);
                startActivity(ig);

            }
        });
    }
}