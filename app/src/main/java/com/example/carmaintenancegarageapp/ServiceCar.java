package com.example.carmaintenancegarageapp;

public class ServiceCar {


    private String name;
    private int imageID;

    public static final ServiceCar[] pizzas = {
            new ServiceCar("jamal", R.drawable.jamal),
            new ServiceCar("jamal", R.drawable.jamal),
            new ServiceCar("jamal", R.drawable.jamal),
            new ServiceCar("jamal", R.drawable.jamal),
            new ServiceCar("jamal", R.drawable.jamal),
            new ServiceCar("jamal", R.drawable.jamal)
    };
    private ServiceCar(String name, int imageID){
        this.name = name;
        this.imageID = imageID;
    }
    public String getName(){return name;}
    public int getImageID(){return imageID;}

}