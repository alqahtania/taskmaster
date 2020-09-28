package com.abdull.taskmaster.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abdull.taskmaster.models.views.VWTaskDuration;
import com.abdull.taskmaster.repositories.RepositoryVWTaskDuration;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class ViewModelVWTaskDurations extends AndroidViewModel {


    private LiveData<List<VWTaskDuration>> allTaskDurations;

    public ViewModelVWTaskDurations(@NonNull Application application) {
        super(application);
        RepositoryVWTaskDuration repositoryVWTaskDuration = new RepositoryVWTaskDuration(application);
        allTaskDurations = repositoryVWTaskDuration.getAllTaskDurations();
    }

    public LiveData<List<VWTaskDuration>> getAllTaskDurations(){
        return allTaskDurations;
    }
}
