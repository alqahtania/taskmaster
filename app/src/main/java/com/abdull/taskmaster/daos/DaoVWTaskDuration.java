package com.abdull.taskmaster.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.abdull.taskmaster.models.views.VWTaskDuration;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */
@Dao
public interface DaoVWTaskDuration {

    @Query("SELECT * FROM VWTaskDuration")
    LiveData<List<VWTaskDuration>> getAllTaskDurations();
}
