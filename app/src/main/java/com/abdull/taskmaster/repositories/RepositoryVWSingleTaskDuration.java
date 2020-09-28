package com.abdull.taskmaster.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.abdull.taskmaster.daos.DaoVWSingleTaskDuration;
import com.abdull.taskmaster.databases.DatabaseTaskMaster;
import com.abdull.taskmaster.models.views.VWSingleTaskDuration;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class RepositoryVWSingleTaskDuration {

    private LiveData<List<VWSingleTaskDuration>> allSingleTaskDurations;
    private DaoVWSingleTaskDuration mDaoVWSingleTaskDuration;

    public RepositoryVWSingleTaskDuration(Application application){
        DatabaseTaskMaster databaseTaskMaster = DatabaseTaskMaster.getInstance(application);
        mDaoVWSingleTaskDuration = databaseTaskMaster.mDaoVWTaskSingleDuration();

    }

    public LiveData<List<VWSingleTaskDuration>> getAllSingleTaskDurations(long taskId){
        allSingleTaskDurations = mDaoVWSingleTaskDuration.getAllSingleTaskDurations(taskId);
        return allSingleTaskDurations;
    }
}
