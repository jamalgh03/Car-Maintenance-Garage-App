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

public class AddNewCarManufacturer extends AppCompatActivity {
    ImageButton add_request;
    ImageButton edit_request_admin;
    ImageButton add_car_new_manufactor;
    ImageButton home;
    ImageButton stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car_manufacturer);




        add_car_new_manufactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AddNewCarManufacturer.this, AddCar.class);
                startActivity(ig);

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AddNewCarManufacturer.this, AdminActivity.class);
                startActivity(ig);

            }
        });
    }
}