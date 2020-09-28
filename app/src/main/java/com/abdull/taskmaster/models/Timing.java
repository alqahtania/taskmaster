package com.abdull.taskmaster.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */
@Entity(indices = {@Index(value = {"task_id"}, unique = false)}, foreignKeys = @ForeignKey(entity = Task.class,
        parentColumns = "id",
        childColumns = "task_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE))
public class Timing {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "task_id")
    private long taskId;
    @ColumnInfo(name = "start_time")
    private long startTime;

    private long duration;
    @ColumnInfo(name = "end_time")
    private long endTime;

    public Timing(long taskId, long startTime, long duration, long endTime) {
        this.taskId = taskId;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public long getTaskId() {
        return taskId;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public long getEndTime() {
        return endTime;
    }

}
