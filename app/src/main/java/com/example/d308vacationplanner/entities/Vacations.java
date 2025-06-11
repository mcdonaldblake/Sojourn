package com.example.d308vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacations {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private double price;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Vacations(int vacationID, String vacationName, double price) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.price = price;
    }
}
