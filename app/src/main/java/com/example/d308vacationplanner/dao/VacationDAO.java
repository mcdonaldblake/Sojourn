package com.example.d308vacationplanner.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.d308vacationplanner.entities.Vacation;
import com.example.d308vacationplanner.entities.VacationWithTotals;

import java.util.List;

@Dao
public interface VacationDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM Vacations ORDER BY vacationID ASC")
    LiveData<List<Vacation>> getAllVacations();

    @Query("SELECT * FROM vacations WHERE vacationID = :vacationId")
    LiveData<Vacation> getVacationById(int vacationId);

    @Transaction
    @Query("SELECT v.*, IFNULL(SUM(e.cost), 0) AS excursionTotal " +
            "FROM vacations v " +
            "LEFT JOIN excursions e ON e.vacationId = v.vacationID " +
            "GROUP BY v.vacationID")
    LiveData<List<VacationWithTotals>> getVacationsWithTotals();

}