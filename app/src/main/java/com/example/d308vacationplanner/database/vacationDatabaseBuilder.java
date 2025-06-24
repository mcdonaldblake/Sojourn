package com.example.d308vacationplanner.database;// This should be the full code for your database class file

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Make sure ALL your entities are listed here
@Database(entities = {Vacation.class, Excursion.class}, version = 1, exportSchema = false)
public abstract class vacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static volatile vacationDatabaseBuilder INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static vacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (vacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    vacationDatabaseBuilder.class, "vacation_database.db")
                            .addCallback(sRoomDatabaseCallback) // This line triggers the data creation
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Get a reference to our DAOs
                VacationDAO mVacationDao = INSTANCE.vacationDAO();
                ExcursionDAO mExcursionDao = INSTANCE.excursionDAO();

                // 1. Create a Vacation. We are setting its ID to 1.
                mVacationDao.insert(new Vacation(1, "Eat Pasta in Italy", "Hotel Roma", "08/10/25", "08/20/25"));

// Vacation 2: Japan
                mVacationDao.insert(new Vacation(2, "Devour Sushi in Japan", "Tokyo Inn", "09/15/25", "09/25/25"));

// Vacation 3: Thailand
                mVacationDao.insert(new Vacation(3, "Noodles in Thailand", "Bangkok Suites", "10/05/25", "10/15/25"));

// Vacation 4: Mexico
                mVacationDao.insert(new Vacation(4, "Tasty Tacos in Mexico", "Cancun Resort", "11/20/25", "11/27/25"));

// --- Don't forget to add your sample excursions here too! ---
// ExcursionDAO mExcursionDao = INSTANCE.excursionDAO();
// mExcursionDao.insert(new Excursion(0, "Colosseum Tour", 150.00, 1)); // Linked to Italy (ID=1)
                // 2. Create Excursions and LINK them using that same ID (1)
                mExcursionDao.insert(new Excursion(0, "Gladiator's Path: Colosseum & Forum Tour", 1));
                mExcursionDao.insert(new Excursion(0, "Venetian Gondola Serenade",  1));
                mExcursionDao.insert(new Excursion(0, "Bullet Train (Shinkansen) Experience",  2));
                mExcursionDao.insert(new Excursion(0, "Day Trip to Majestic Mt. Fuji",  2));
                mExcursionDao.insert(new Excursion(0, "Chichen Itza: Wonder of the World Tour",  4));
                mExcursionDao.insert(new Excursion(0, "Swimming in a Sacred Cenote", 4));
                mExcursionDao.insert(new Excursion(0, "Ethical Elephant Sanctuary Encounter", 3));
                mExcursionDao.insert(new Excursion(0, "Muay Thai Boxing: Ringside Experience", 3));


            });
        }
    };
}