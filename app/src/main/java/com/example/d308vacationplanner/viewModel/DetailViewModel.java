package com.example.d308vacationplanner.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.d308vacationplanner.database.Repository;
import com.example.d308vacationplanner.entities.Excursion;
import com.example.d308vacationplanner.entities.Vacation;
import java.util.List;
import com.example.d308vacationplanner.database.VacationDatabaseBuilder;



public class DetailViewModel extends AndroidViewModel {
    private Repository mRepository;

    private final MutableLiveData<String> mToastMessage = new MutableLiveData<>();

    public DetailViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    public LiveData<String> getToastMessage() {
        return mToastMessage;
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

    public void deleteVacation (Vacation vacation) {
        Repository.databaseExecutor.execute(() -> {
            int vacationId = vacation.getVacationID();

            int excursionCount = mRepository.getExcursionCountForVacation(vacationId);

            if (excursionCount == 0) {
                mRepository.delete(vacation);

                mToastMessage.postValue("Vacation deleted successfully.");
            } else {
                mToastMessage.postValue("Cannot delete a vacation that has associated excursions.");
            }
            });

        }
    }
