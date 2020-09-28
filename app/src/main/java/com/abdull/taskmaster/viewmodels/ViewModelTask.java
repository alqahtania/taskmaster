package com.abdull.taskmaster.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abdull.taskmaster.models.Task;
import com.abdull.taskmaster.repositories.RepositoryTask;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */
public class ViewModelTask extends AndroidViewModel {

    private RepositoryTask  mRepositoryTask;
    private LiveData<List<Task>> allTasks;
    private LiveData<Task> runningTask;
    //When editing we store the task from mainfrag here so addedit can grab it
    private MutableLiveData<Task> mTaskToEdit = new MutableLiveData<>();
    private MutableLiveData<Task> mTaskToDelete = new MutableLiveData<>();

    public ViewModelTask(@NonNull Application application) {
        super(application);
        this.mRepositoryTask = new RepositoryTask(application);
        this.allTasks = mRepositoryTask.getAllTasks();
        this.runningTask = mRepositoryTask.getRunningTask();
    }

    public void selectTaskToEdit(Task task){
        mTaskToEdit.setValue(task);
    }

    public LiveData<Task> getSelectedTaskToEdit(){
        return mTaskToEdit;
    }

    public void selectTaskToDelete(Task task){
        mTaskToDelete.setValue(task);
    }
    public LiveData<Task> getSelectedTaskToDelete(){
        return mTaskToDelete;
    }


    public void insert(Task task){
        mRepositoryTask.insert(task);
    }

    public void update(Task task){
        mRepositoryTask.update(task);
    }

    public void delete(Task task){
        mRepositoryTask.delete(task);
    }

    public void deleteAllTasks(){
        mRepositoryTask.deleteAllTasks();
    }

    public void setRunningTask(Task task){
        mRepositoryTask.setRunningTask(task);
    }

    public LiveData<Task> getRunningTask(){
        return runningTask;
    }


    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }



}
