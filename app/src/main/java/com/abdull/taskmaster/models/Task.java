package com.abdull.taskmaster.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String name;

    private String description;

    @ColumnInfo(name = "sort_order")
    private int sortOrder;
    private boolean running;

    public Task(@NonNull String name, String description, int sortOrder) {
        this.name = name;
        this.description = description;
        this.sortOrder = sortOrder;
        this.running = running;
    }

    public long getId() {
        return id;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }



    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isRunning() {
        return running;
    }
}
