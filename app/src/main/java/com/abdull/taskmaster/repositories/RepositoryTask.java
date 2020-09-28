package com.abdull.taskmaster.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.abdull.taskmaster.app.MyApplication;
import com.abdull.taskmaster.daos.DaoTask;
import com.abdull.taskmaster.databases.DatabaseTaskMaster;
import com.abdull.taskmaster.models.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */
public class RepositoryTask {

    private DaoTask mDaoTask;
    private LiveData<List<Task>> allTasks;
    private LiveData<Task> runningTask;
    private ExecutorService mExecutorService;

    public RepositoryTask(Application application){
        DatabaseTaskMaster databaseTaskMaster = DatabaseTaskMaster.getInstance(application);
        mDaoTask = databaseTaskMaster.mDaoTask();
        allTasks = mDaoTask.getAllTasks();
        runningTask = mDaoTask.getRunningTask();
        mExecutorService = MyApplication.getExecutorServiceInstance();
    }

    public void insert(final Task task){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTask.insert(task);
            }
        });
    }

    public void update(final Task task){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTask.update(task);
            }
        });
    }

    public void delete(final Task task){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTask.delete(task);
            }
        });
    }

    public void deleteAllTasks(){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTask.deleteAllTasks();
            }
        });
    }
    public void setRunningTask(final Task task){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mDaoTask.updateRunningFieldToFalse();
                mDaoTask.update(task);
            }
        });
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public LiveData<Task> getRunningTask(){
        return runningTask;
    }




}
