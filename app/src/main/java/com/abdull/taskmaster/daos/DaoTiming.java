package com.abdull.taskmaster.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.abdull.taskmaster.models.Timing;

import java.util.List;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */

@Dao
public interface DaoTiming {

    @Insert
    void insert(Timing timing);

    @Update
    void update(Timing timing);

    @Delete
    void delete(Timing timing);

    @Query("DELETE FROM Timing")
    void deleteAllTimings();

    @Query("SELECT * FROM Timing")
    LiveData<List<Timing>> getAllTimings();

    @Query("SELECT * FROM Timing WHERE duration = 0 AND end_time = 0")
    LiveData<Timing> getRunningTiming();



}
