package com.abdull.taskmaster.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.abdull.taskmaster.models.Task;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */

@Dao
public interface DaoTask {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM Task")
    void deleteAllTasks();

    @Query("SELECT * FROM Task ORDER BY sort_order, name COLLATE NOCASE")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM Task WHERE running = 1")
    LiveData<Task> getRunningTask();

    @Query("UPDATE Task SET running = 0")
    void updateRunningFieldToFalse();


}
