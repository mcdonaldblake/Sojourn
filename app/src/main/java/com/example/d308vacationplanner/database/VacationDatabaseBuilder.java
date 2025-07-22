package com.example.d308vacationplanner.database;// This should be the full code for your database class file

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Make sure ALL your entities are listed here
@Database(entities = {Vacation.class, Excursion.class}, version = 3, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static final Migration Migration_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE vacations ADD COLUMN cost REAL NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE excursions ADD COLUMN cost REAL NOT NULL DEFAULT 0");
        }
    };
    public static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    VacationDatabaseBuilder.class, "vacation_database.db")
                            .addMigrations(Migration_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}