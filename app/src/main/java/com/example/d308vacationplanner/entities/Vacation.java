package com.example.d308vacationplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private String hotel;
    private String startDate;
    private String endDate;

    public Vacation(int vacationID, String vacationName, String hotel, String startDate, String endDate) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.hotel = hotel;
        this.startDate = startDate;
        this.endDate = endDate;
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

}
