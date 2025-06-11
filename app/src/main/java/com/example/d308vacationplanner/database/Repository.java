package com.example.d308vacationplanner.database;

import android.app.Application;

import com.example.d308vacationplanner.dao.ExcursionDAO;
import com.example.d308vacationplanner.dao.VacationDAO;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final ExcursionDAO mExcursionDAO;
    private final VacationDAO mVacationDAO;

    private List<Vacations> mAllVacations;
    private List<Excursion> mAllExcursions;

    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutors = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        vacationDatabaseBuilder db = vacationDatabaseBuilder.getDatabase(application);
        mExcursionDAO = db.excursionDAO();
        mVacationDAO = db.vacationDAO();
    }

    public List<Excursion>getmAllExcursions(){
        databaseExecutors.execute(() ->{
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllExcursions;

    }
    public void delete(Excursion excursion) {
        databaseExecutors.execute(() -> {
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Excursion excursion) {
        databaseExecutors.execute(() -> {
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void insert(Excursion excursion) {
        databaseExecutors.execute(() -> {
            mExcursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
    public List<Vacations>getmAllVacations(){
        databaseExecutors.execute(() ->{
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllVacations;

    }
    public void insert(Vacations vacations) {
        databaseExecutors.execute(() -> {
            mVacationDAO.insert(vacations);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    public void update(Vacations vacations){
        databaseExecutors.execute(() -> {
            mVacationDAO.update(vacations);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

            }
        }
    public void delete(Vacations vacations) {
        databaseExecutors.execute(() -> {
            mVacationDAO.delete(vacations);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }
    public List<Excursion>getAssociatedExcursions(int vacationID){
        databaseExecutors.execute(() ->{
            mAllExcursions = mExcursionDAO.getAssociatedExcursions(vacationID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllExcursions;

    }
}
