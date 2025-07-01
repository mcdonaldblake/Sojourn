package com.example.d308vacationplanner.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;
import java.util.List;
import com.example.d308vacationplanner.database.VacationDatabaseBuilder;



public class DetailViewModel extends AndroidViewModel {
    private Repository mRepository;

    public DetailViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
    }
    // Add this new method inside your DetailViewModel.java class

    public LiveData<List<Excursion>> getExcursionCountForVacation(int vacationId) {
        return mRepository.getAllExcursions(vacationId);
    }

    public LiveData<List<Excursion>> getAssociatedExcursions(int vacationId) {
        return mRepository.getAssociatedExcursions(vacationId);
    }

    public void insert(Excursion excursion) {
        mRepository.insert(excursion);
    }

    public void update(Excursion excursion) {
        mRepository.update(excursion);
    }

    public void deleteExcursionById(int excursionId) {
        VacationDatabaseBuilder.databaseWriteExecutor.execute(() -> {
            VacationDatabaseBuilder
                    .getDatabase(getApplication())
                    .excursionDAO()
                    .deleteById(excursionId);
        });
    }


    public LiveData<Vacation> getVacationById(int vacationId) {
        return mRepository.getVacationById(vacationId);
    }

    public void update(Vacation vacation) {
        mRepository.update(vacation);
    }

    public void delete(Vacation vacation) {
        mRepository.delete(vacation);
    }
}