package com.example.carmaintenancegarageapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    LinearLayout pending_filed;
    ImageButton add_request;
    ImageButton edit_request_admin;
    ImageButton add_car_admin;
    ImageButton more_options;
    ImageButton stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        pending_filed = findViewById(R.id.pendingfiled);
        add_request = findViewById(R.id.addrequestadmin);
        edit_request_admin = findViewById(R.id.editrequestadmin);
        add_car_admin = findViewById(R.id.addcaradmin);
        more_options = findViewById(R.id.moreoptions);
        stats = findViewById(R.id.stats);
        pending_filed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AdminActivity.this, About.class);
                startActivity(ig);

            }
        });
        add_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AdminActivity.this, AddRequestAdmin.class);
                startActivity(ig);

            }
        });
        edit_request_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AdminActivity.this, EditRequest.class);
                startActivity(ig);

            }
        });
        add_car_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AdminActivity.this, AddAdmin.class);
                startActivity(ig);

            }
        });
        more_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AdminActivity.this, MoreOptions.class);
                startActivity(ig);

            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ig = new Intent(AdminActivity.this, StatisticsActivity.class);
                startActivity(ig);

            }
        });
    }
}
