package com.example.d308vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "vacations")
public class Vacation implements Costable{
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private String hotel;
    private String startDate;
    private String endDate;
    private double cost;

    public Vacation(int vacationID, String vacationName, String hotel, String startDate, String endDate, double cost) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
        setCost(cost);
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public void setHotel(String hotel) {this.hotel = hotel;}
    public String getHotel() {return hotel;}

    public void setStartDate(String startDate) {this.startDate = startDate;}
    public String getStartDate() {return startDate;}

    public void setEndDate(String endDate) {this.endDate = endDate;}
    public String getEndDate(){return endDate;}

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        if (cost < 0) throw new IllegalArgumentException("Cost can not be negative");
        this.cost = cost;
    }

}
