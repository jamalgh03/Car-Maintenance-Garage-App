package com.example.carmaintenancegarageapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Handler handler = new Handler();
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] captions = new String[ServiceCar.pizzas.length];
        int[] imageIds = new int[ServiceCar.pizzas.length];

        for (int i = 0; i < ServiceCar.pizzas.length; i++) {
            captions[i] = ServiceCar.pizzas[i].getName();
            imageIds[i] = ServiceCar.pizzas[i].getImageID();
        }

        // إعداد الـ RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(captions, imageIds);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        startAutoScroll();
    }

    private void startAutoScroll() {
        Runnable autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (recyclerView != null && recyclerView.getAdapter() != null) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int itemCount = layoutManager.getItemCount();
                        if (itemCount > 0) {
                            currentPosition = (currentPosition + 1) % itemCount;
                            layoutManager.smoothScrollToPosition(recyclerView, null, currentPosition);
                        }
                    }
                }
                handler.postDelayed(this, 3000);
            }
        };

        handler.post(autoScrollRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
