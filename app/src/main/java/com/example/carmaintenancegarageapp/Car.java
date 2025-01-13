package com.example.carmaintenancegarageapp;

public class Car {
    private String carName;
    private String carModel;
    private String carNumber;
    private String email;

    public Car(String carName, String carModel, String carNumber) {
        this.carName = carName;
        this.carModel = carModel;
        this.carNumber = carNumber;
    }

    // Getter and Setter methods
    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
