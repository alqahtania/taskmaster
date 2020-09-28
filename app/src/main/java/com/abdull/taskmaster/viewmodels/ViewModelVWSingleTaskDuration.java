package com.abdull.taskmaster.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abdull.taskmaster.models.views.VWSingleTaskDuration;
import com.abdull.taskmaster.repositories.RepositoryVWSingleTaskDuration;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class ViewModelVWSingleTaskDuration extends AndroidViewModel {

    private LiveData<List<VWSingleTaskDuration>> allSingleTaskDurations;
    private RepositoryVWSingleTaskDuration mRepositoryVWSingleTaskDuration;

    public ViewModelVWSingleTaskDuration(@NonNull Application application) {
        super(application);
        mRepositoryVWSingleTaskDuration = new RepositoryVWSingleTaskDuration(application);
    }

    public LiveData<List<VWSingleTaskDuration>> getAllSingleTaskDurations(long taskId){
        allSingleTaskDurations = mRepositoryVWSingleTaskDuration.getAllSingleTaskDurations(taskId);
        return allSingleTaskDurations;
    }

}
