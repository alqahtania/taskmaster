package com.abdull.taskmaster.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abdull.taskmaster.models.Timing;
import com.abdull.taskmaster.repositories.RepositoryTiming;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class ViewModelTiming extends AndroidViewModel {

    private RepositoryTiming mRepositoryTiming;
    private LiveData<List<Timing>> allTimings;
    private LiveData<Timing> runningTiming;
    public ViewModelTiming(@NonNull Application application) {
        super(application);
        mRepositoryTiming = new RepositoryTiming(application);
        allTimings = mRepositoryTiming.getAllTimings();
        runningTiming = mRepositoryTiming.getRunningTiming();
    }


    public void insert(Timing timing){
        mRepositoryTiming.insert(timing);
    }

    public void update(Timing timing){
        mRepositoryTiming.update(timing);
    }

    public void delete(Timing timing){
        mRepositoryTiming.delete(timing);
    }

    public void deleteAllTimings(){
        mRepositoryTiming.deleteAllTimings();
    }

    public LiveData<List<Timing>> getAllTimings(){
        return allTimings;
    }

    public LiveData<Timing> getRunningTiming(){
        return runningTiming;
    }
}
