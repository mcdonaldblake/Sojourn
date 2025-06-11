package com.example.d308vacationplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacations;

@Database(entities = {Vacations.class, Excursion.class}, version = 4, exportSchema = false)
public abstract class vacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile vacationDatabaseBuilder INSTANCE;

    static vacationDatabaseBuilder getDatabase(final Context context) {
        if(INSTANCE==null) {
            synchronized (vacationDatabaseBuilder.class) {
                if(INSTANCE==null) {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(), vacationDatabaseBuilder.class, "MyVacationDatabase.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
