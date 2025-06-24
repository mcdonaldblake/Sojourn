package com.example.d308vacationplanner.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;
import java.util.List;

public class DetailViewModel extends AndroidViewModel {
    private Repository mRepository;

    public DetailViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
    }
    // Add this new method inside your DetailViewModel.java class

    public int getExcursionCountForVacation(int vacationId) {
        return mRepository.getExcursionCountForVacation(vacationId);
    }

    public void delete(Vacation vacation) {
        mRepository.delete(vacation);
    }


public LiveData<List<Excursion>> getAssociatedExcursions(int vacationId) {
        return mRepository.getAssociatedExcursions(vacationId);
    }

    public void update(Vacation vacation) {
        mRepository.update(vacation);
    }

    // This method is for inserting a new Excursion
    public void insert(Excursion excursion) {
        mRepository.insert(excursion);
    }

    public LiveData<Vacation> getVacationById(int vacationId) {
        return mRepository.getVacationById(vacationId);
    }
}
