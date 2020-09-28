package com.abdull.taskmaster.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.abdull.taskmaster.app.MyApplication;
import com.abdull.taskmaster.daos.DaoTiming;
import com.abdull.taskmaster.databases.DatabaseTaskMaster;
import com.abdull.taskmaster.models.Timing;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class RepositoryTiming {

    private DaoTiming mDaoTiming;
    private LiveData<List<Timing>> allTimings;
    private LiveData<Timing> runningTiming;
    private ExecutorService mExecutorService;

    public RepositoryTiming(Application application){
        DatabaseTaskMaster databaseTaskMaster = DatabaseTaskMaster.getInstance(application);
        mDaoTiming = databaseTaskMaster.mDaoTiming();
        allTimings = mDaoTiming.getAllTimings();
        runningTiming = mDaoTiming.getRunningTiming();
        mExecutorService = MyApplication.getExecutorServiceInstance();
    }

    public void insert(final Timing timing){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTiming.insert(timing);
            }
        });
    }

    public void update(final Timing timing){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTiming.update(timing);
            }
        });
    }

    public void delete(final Timing timing){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTiming.delete(timing);
            }
        });
    }

    public void deleteAllTimings(){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTiming.deleteAllTimings();
            }
        });
    }

    public LiveData<List<Timing>> getAllTimings(){
        return allTimings;
    }

    public LiveData<Timing> getRunningTiming(){
        return runningTiming;
    }
}
