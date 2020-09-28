package com.abdull.taskmaster.models.views;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */

@DatabaseView("SELECT Timing.id AS timing_id, Task.name AS task_name, Task.description AS task_description, Timing.start_Time AS " +
        "timing_start_time, " +
        "DATE(Timing.start_time, 'unixepoch', 'localtime') AS start_date, " +
        "SUM(Timing.duration) AS total_duration " +
        "FROM Task INNER JOIN Timing ON Task.id = Timing.task_id " +
        "GROUP BY Task.id, start_date")
public class VWTaskDuration {

    @ColumnInfo(name = "timing_id")
    private long timingId;

    @ColumnInfo(name = "task_name")
    private String taskName;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @ColumnInfo(name = "timing_start_time")
    private long timingStartTime;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "total_duration")
    private long totalDuration;

    public VWTaskDuration(String taskName, String taskDescription, long timingStartTime, String startDate, long totalDuration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.timingStartTime = timingStartTime;
        this.startDate = startDate;
        this.totalDuration = totalDuration;
    }

    public void setTimingId(long timingId) {
        this.timingId = timingId;
    }

    public long getTimingId() {
        return timingId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public long getTimingStartTime() {
        return timingStartTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public long getTotalDuration() {
        return totalDuration;
    }
}
