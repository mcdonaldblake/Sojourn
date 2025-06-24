package com.example.d308vacationplanner.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308vacationplanner.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Excursion excursion);

    @Update
    void update (Excursion excursion);

    @Delete
    void delete (Excursion excursion);

    @Query("SELECT * FROM EXCURSIONS ORDER BY excursionID ASC")
    LiveData<List<Excursion>> getAllExcursions();

    @Query("SELECT * FROM EXCURSIONS WHERE vacationID = :vacationID ORDER BY excursionID ASC")
    LiveData<List<Excursion>> getAssociatedExcursions(int vacationID);

    @Query("SELECT COUNT(*) FROM excursions WHERE vacationID = :vacationId")
    int getExcursionCountForVacation(int vacationId);


}
