package com.example.d308vacationplanner.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class VacationWithTotals {
    @Embedded
    public Vacation vacation;

    @ColumnInfo(name = "excursionTotal")
    public double excursionTotal;

    public double getTotalCost() {
        return vacation.getCost() + excursionTotal;
    }

}
