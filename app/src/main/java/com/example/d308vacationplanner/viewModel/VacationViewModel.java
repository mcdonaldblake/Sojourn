package com.example.d308vacationplanner.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Vacation;

import java.util.List;

public class VacationViewModel extends AndroidViewModel {
    private Repository mRepository;
    private LiveData<List<Vacation>> mAllVacations;

    public VacationViewModel(@NonNull Application application) {
        super(application);

        mRepository = new Repository(application);
        mAllVacations = mRepository.getAllVacations();
    }

    public LiveData<List<Vacation>> getAllVacations(){
        return mAllVacations;
    }

    public void insert(Vacation vacation) {
        mRepository.insert(vacation);
    }

}
