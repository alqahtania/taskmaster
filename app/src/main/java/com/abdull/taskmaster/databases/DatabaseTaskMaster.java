package com.abdull.taskmaster.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abdull.taskmaster.daos.DaoTask;
import com.abdull.taskmaster.daos.DaoTiming;
import com.abdull.taskmaster.daos.DaoVWSingleTaskDuration;
import com.abdull.taskmaster.daos.DaoVWTaskDuration;
import com.abdull.taskmaster.models.Task;
import com.abdull.taskmaster.models.Timing;
import com.abdull.taskmaster.models.views.VWSingleTaskDuration;
import com.abdull.taskmaster.models.views.VWTaskDuration;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */

@Database(entities = {Task.class, Timing.class}, views = {VWTaskDuration.class, VWSingleTaskDuration.class}, version = 1, exportSchema = false)
public abstract class DatabaseTaskMaster extends RoomDatabase {

    private static final String dbName = "task_master";
    private static DatabaseTaskMaster instance;

    public abstract DaoTask mDaoTask();
    public abstract DaoTiming mDaoTiming();
    public abstract DaoVWTaskDuration mDaoVWTaskDuration();
    public abstract DaoVWSingleTaskDuration mDaoVWTaskSingleDuration();

    public static synchronized DatabaseTaskMaster getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DatabaseTaskMaster.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
