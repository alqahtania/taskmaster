package com.abdull.taskmaster.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.abdull.taskmaster.models.views.VWSingleTaskDuration;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */

@Dao
public interface DaoVWSingleTaskDuration {

    @Query("SELECT * FROM VWSingleTaskDuration WHERE task_id = :taskId")
    LiveData<List<VWSingleTaskDuration>> getAllSingleTaskDurations(long taskId);

}
