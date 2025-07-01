package com.example.d308vacationplanner.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;

public class Repository {
    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        // Use your actual database class name here
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return mVacationDAO.getAllVacations();
    }


    public LiveData<List<Excursion>> getAssociatedExcursions(int vacationID) {
        return mExcursionDAO.getAssociatedExcursions(vacationID);
    }

    public LiveData<Vacation> getVacationById(int vacationId) {
        return mVacationDAO.getVacationById(vacationId);
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() ->
                mVacationDAO.insert(vacation));
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() ->
                mVacationDAO.update(vacation));
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() ->
                mVacationDAO.delete(vacation));
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() ->
                mExcursionDAO.insert(excursion));
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() ->
                mExcursionDAO.insert(excursion));
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() ->
                mExcursionDAO.insert(excursion));
    }

    public LiveData<List<Excursion>> getAllExcursions(int vacationId) {
    return mExcursionDAO.getAllExcursions();
    }
}