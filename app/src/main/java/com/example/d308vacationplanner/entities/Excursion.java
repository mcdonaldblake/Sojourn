package com.example.d308vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "vacationID",
                childColumns = "vacationID",
                onDelete = ForeignKey.RESTRICT))
public class Excursion implements Costable {

    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private int vacationID;
    private String excursionDate;
    private double cost;

    public Excursion(int excursionID, String excursionName, String excursionDate, int vacationID, double cost) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.excursionDate = excursionDate;
        this.vacationID = vacationID;
        setCost(cost);
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }

    public String getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(String excursionDate) {
        this.excursionDate = excursionDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        if (cost < 0) throw new IllegalArgumentException("Cost can not be negative");
        this.cost = cost;
    }
}