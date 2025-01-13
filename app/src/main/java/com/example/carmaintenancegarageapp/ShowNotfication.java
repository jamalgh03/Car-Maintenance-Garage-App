package com.example.carmaintenancegarageapp;

public class ShowNotfication {
    private String CarName;
    private String CarNumber;
    private String Stauts ;
    private String msg ;

    public ShowNotfication(String carName, String carNumber, String stauts, String msg) {
        CarName = carName;
        CarNumber = carNumber;
        Stauts = stauts;
        this.msg = msg;
    }

    public String getCarName() {
        return CarName;
    }

    public void setCarName(String carName) {
        CarName = carName;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getStauts() {
        return Stauts;
    }

    public void setStauts(String stauts) {
        Stauts = stauts;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

