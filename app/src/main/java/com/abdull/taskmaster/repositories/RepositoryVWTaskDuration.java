package com.abdull.taskmaster.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.abdull.taskmaster.daos.DaoVWTaskDuration;
import com.abdull.taskmaster.databases.DatabaseTaskMaster;
import com.abdull.taskmaster.models.views.VWTaskDuration;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class RepositoryVWTaskDuration {


    private LiveData<List<VWTaskDuration>> allTaskDurations;

    public RepositoryVWTaskDuration(Application application){
        DatabaseTaskMaster databaseTaskMaster = DatabaseTaskMaster.getInstance(application);
        DaoVWTaskDuration daoVWTaskDuration = databaseTaskMaster.mDaoVWTaskDuration();
        allTaskDurations = daoVWTaskDuration.getAllTaskDurations();
    }

    public LiveData<List<VWTaskDuration>> getAllTaskDurations(){
        return allTaskDurations;
    }


}
