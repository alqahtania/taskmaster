package com.abdull.taskmaster.app;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Abdullah Alqahtani on 9/22/2020.
 */
public abstract class MyApplication extends Application {

    private static ExecutorService instance;

    public static synchronized ExecutorService getExecutorServiceInstance(){
        if(instance == null){
            instance = Executors.newCachedThreadPool();
        }
        return instance;
    }
}
