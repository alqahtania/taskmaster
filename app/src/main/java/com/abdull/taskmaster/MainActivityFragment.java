package com.abdull.taskmaster;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdull.taskmaster.models.Task;
import com.abdull.taskmaster.models.Timing;
import com.abdull.taskmaster.viewmodels.ViewModelTask;
import com.abdull.taskmaster.viewmodels.ViewModelTiming;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class MainActivityFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private static final String TAG = "MainActivityFragment";

    private ViewModelTask mViewModelTask;
    private ViewModelTiming mViewModelTiming;
    private TaskAdapter mTaskAdapter;
    private CountUpTimer mCountUpTimer;




    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView currentTask = view.findViewById(R.id.current_task);
        final TextView currentTaskTimer = view.findViewById(R.id.current_task_timer);
        RecyclerView recyclerView = view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mTaskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(mTaskAdapter);

        mViewModelTiming = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(ViewModelTiming.class);
        mViewModelTask = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(ViewModelTask.class);
        mViewModelTask.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                if (mTaskAdapter != null) {
                    mTaskAdapter.submitList(tasks);
                }
            }
        });

        mViewModelTask.getRunningTask().observe(getViewLifecycleOwner(), new Observer<Task>() {
            @Override
            public void onChanged(Task task) {
                boolean taskIsNull = task == null;
                Log.d(TAG, "onChanged: Task is null " + taskIsNull);
                if(task != null){
                    Log.d(TAG, "onChanged: TaskName: " + task.getName());
                    currentTask.setText(task.getName());
                    currentTask.setVisibility(View.VISIBLE);
                }else {

                    currentTask.setVisibility(View.GONE);
                }
            }
        });
        mViewModelTiming.getRunningTiming().observe(getViewLifecycleOwner(), new Observer<Timing>() {
            @Override
            public void onChanged(Timing timing) {
                boolean currTiming = timing == null;
                Log.d(TAG, "onChanged: Curring timing null? " + currTiming);
                if(!currTiming){
                    Log.d(TAG, "onChanged: curr timing taskid " + timing.getTaskId());
                    setTimerTextView(timing.getStartTime(), currentTaskTimer);
                }else {
                    currentTaskTimer.setVisibility(View.GONE);
                }
            }
        });


        return view;
    }

    private void setTimerTextView(long startTime, TextView textView) {

        Date date = new Date();

        long currentTime = date.getTime();
        long currentTaskTimerMillis = currentTime - (startTime * 1000); //because startTime is stored in seconds, we need millis

        long seconds = TimeUnit.MILLISECONDS.toSeconds(currentTaskTimerMillis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTaskTimerMillis));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTaskTimerMillis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(currentTaskTimerMillis));

        long hours = TimeUnit.MILLISECONDS.toHours(currentTaskTimerMillis) -
                (TimeUnit.MILLISECONDS.toHours(currentTaskTimerMillis) / 24) * 24;
        long days = TimeUnit.MILLISECONDS.toHours(currentTaskTimerMillis) / 24;

        Log.d(TAG, "setSharedPrefTimer: seconds are " + seconds +
                "\n mins are " + minutes +
                "\n hours are " + hours +
                "\n days are " + date);


        if (mCountUpTimer != null) {
            mCountUpTimer.cancel();
            mCountUpTimer.resetTimer();
        }
        mCountUpTimer = new CountUpTimer(Long.MAX_VALUE, 1000, textView);
        mCountUpTimer.setSecondsCountUp((int) seconds);
        mCountUpTimer.setMinutesCountUp((int) minutes);
        mCountUpTimer.setHoursCountUp((int) hours);
        mCountUpTimer.setDaysCountUp((int) days);
        mCountUpTimer.start();
        textView.setVisibility(View.VISIBLE);


    }

    @Override
    public void onEditClick(@NonNull Task task) {
        mViewModelTask.selectTaskToEdit(task);
        ((MainActivity) getActivity()).goToEditFragment();
    }

    @Override
    public void onDeleteClick(@NonNull Task task) {
        ((MainActivity)getActivity()).onDeleteClick(task);
    }

    @Override
    public void onStartTaskClicked(@NonNull Task task) {
        Timing currentRunningTiming = mViewModelTiming.getRunningTiming().getValue();
        boolean nullCurrent = currentRunningTiming == null;
        Log.d(TAG, "onStartTaskClicked: null currentrunningtiming? "+ nullCurrent);
        Date currentTime = new Date();
        if(currentRunningTiming != null){
            if(task.getId() == currentRunningTiming.getTaskId()) {
                Log.d(TAG, "onStartTaskClicked: The current task was tapped a second time Name: "+ task.getName() +
                        " TimingTaskId: " + currentRunningTiming.getTaskId() + " TimingId: " + currentRunningTiming.getId());
                //The current task was tapped a second time, so stop timing it
                long durartion = (currentTime.getTime() / 1000) - currentRunningTiming.getStartTime();
                long endTime = currentTime.getTime() / 1000;
                long timingId = currentRunningTiming.getId();
                long taskId = task.getId();
                currentRunningTiming.setDuration(durartion);
                currentRunningTiming.setEndTime(endTime);
                currentRunningTiming.setId(timingId);
                task.setRunning(false);
                task.setId(taskId);
                mViewModelTiming.update(currentRunningTiming);
                mViewModelTask.update(task);
            }else {
                Log.d(TAG, "onStartTaskClicked: a new task is being timed so stop the old one first");
                //a new task is being timed so stop the old one first
                long durartion = (currentTime.getTime() / 1000) - currentRunningTiming.getStartTime();
                long endTime = currentTime.getTime() / 1000;
                currentRunningTiming.setDuration(durartion);
                currentRunningTiming.setEndTime(endTime);
                Task oldRunningTask = mViewModelTask.getRunningTask().getValue();
                if(oldRunningTask != null) {
                    long oldTaskId = oldRunningTask.getId();
                    oldRunningTask.setRunning(false);
                    oldRunningTask.setId(oldTaskId);
                    mViewModelTask.update(oldRunningTask);
                    mViewModelTiming.update(currentRunningTiming);
                    //Insert a new timing for the new task and update it to running
                    long newRunningTaskId = task.getId();
                    task.setRunning(true);
                    task.setId(newRunningTaskId);
                    mViewModelTask.setRunningTask(task);
                    long startTime = currentTime.getTime() / 1000;
                    Timing timing = new Timing(task.getId(), startTime, 0, 0);
                    mViewModelTiming.insert(timing);
                }
            }
        }else {
            Log.d(TAG, "onStartTaskClicked: no task being timed, so start timing the new task ");
            //no task being timed, so start timing the new task
            long startTime = currentTime.getTime() / 1000;
            Timing newTiming = new Timing(task.getId(), startTime, 0, 0);
            mViewModelTiming.insert(newTiming);
            long taskId = task.getId();
            task.setRunning(true);
            task.setId(taskId);
            mViewModelTask.setRunningTask(task);

        }
    }

    @Override
    public void onShowReportClicked(@NonNull Task task) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.showSingleTaskReport(task);
        }
    }


}
