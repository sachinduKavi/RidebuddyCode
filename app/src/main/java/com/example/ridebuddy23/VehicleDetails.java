package com.example.ridebuddy23;

import androidx.annotation.NonNull;

public class VehicleDetails {
    String vehicleType, imageLink, regNum, chassisNumber, level;
    int seats;

    VehicleDetails(String vehicleType, String imageLink, String regNum, String chassisNumber, String level, int seats){
        this.vehicleType = vehicleType;
        this.imageLink = imageLink;
        this.regNum = regNum;
        this.chassisNumber = chassisNumber;
        this.level = level;
        this.seats =seats;
    }
    VehicleDetails(){}
    @NonNull
    public String toString(){
        return this.regNum;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }


}
