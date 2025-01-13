package com.example.carmaintenancegarageapp;

public class ServiceCar {

    private String name;
    private String imageUrl;  // أو أي نوع آخر للبيانات التي تجلبها من قاعدة البيانات

    public ServiceCar(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
