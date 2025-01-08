package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MoreOptions extends AppCompatActivity {
    LinearLayout profile;
    LinearLayout notifications;
    LinearLayout contact;
    LinearLayout about;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);
        profile = findViewById(R.id.profile);
        contact = findViewById(R.id.contact);
        notifications = findViewById(R.id.notifications);
        about = findViewById(R.id.about);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(MoreOptions.this, ProfileActivity.class);
                startActivity(ig);

            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(MoreOptions.this, Notfication.class);
                startActivity(ig);

            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(MoreOptions.this, Contact.class);
                startActivity(ig);

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(MoreOptions.this, About.class);
                startActivity(ig);

            }
        });
    }
}