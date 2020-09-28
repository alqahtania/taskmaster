package com.abdull.taskmaster.models.views;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */
@DatabaseView("SELECT Task.id AS task_id, Timing.task_id AS timing_task_id , Task.name AS task_name, " +
        "Task.description AS task_description, DATETIME(Timing.start_time, 'unixepoch', 'localtime') AS start_date, " +
        "Timing.start_time AS timing_start_time," +
        " Timing.duration AS timing_duration, Timing.end_time AS timing_end_time FROM Task INNER JOIN Timing ON Task.id = Timing.task_id")
public class VWSingleTaskDuration {

    @ColumnInfo(name = "task_id")
    private long taskId;
    @ColumnInfo(name = "timing_task_id")
    private long timingTaskId;

    @ColumnInfo(name = "task_name")
    private String taskName;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "timing_start_time")
    private long timingStartTime;

    @ColumnInfo(name = "timing_duration")
    private long timingDuration;

    @ColumnInfo(name = "timing_end_time")
    private long timingEndTime;

    public VWSingleTaskDuration(long taskId, long timingTaskId, String taskName, String taskDescription, String startDate, long timingStartTime, long timingDuration, long timingEndTime) {
        this.taskId = taskId;
        this.timingTaskId = timingTaskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.timingStartTime = timingStartTime;
        this.timingDuration = timingDuration;
        this.timingEndTime = timingEndTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public long getTimingTaskId() {
        return timingTaskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public long getTimingStartTime() {
        return timingStartTime;
    }

    public long getTimingDuration() {
        return timingDuration;
    }

    public long getTimingEndTime() {
        return timingEndTime;
    }
}
