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

public class payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);

        ImageButton paypalButton = findViewById(R.id.imageButton10);
        ImageButton visaButton = findViewById(R.id.imageButton11);

        paypalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment.this, PayByPayPal.class); // Replace PayPalActivity with your actual target activity name
                startActivity(intent);
            }
        });


        visaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(payment.this, PayByVisa.class); // Replace VisaActivity with your actual target activity name
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
                Intent intent = new Intent(payment.this, MainActivity.class);
                startActivity(intent);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment.this, AddRequest.class);
                startActivity(intent);
            }
        });

        carButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(payment.this, AddCar.class);
                startActivity(intent);
            }
        });

          menuButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
          Intent intent = new Intent(payment.this, MoreOptions.class);
          startActivity(intent);
          }
         });

    }
}