package com.abdull.taskmaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.abdull.taskmaster.models.Task;
import com.abdull.taskmaster.viewmodels.ViewModelTask;

import java.util.TreeSet;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class AddEditFragment extends Fragment {

    private static final String TAG = "AddEditFragment";

    private enum FragmentEditMode {EDIT, ADD}

    private FragmentEditMode mMode;

    private EditText mNameTextView;
    private EditText mDescriptionTextView;
    private EditText mSortOrderTextView;
    private boolean mCanCloseWhenBackBtnPrssd = false;
    private ViewModelTask mViewModelTask;




    public AddEditFragment() {
        Log.d(TAG, "AddEditActivityFragment: constructor called");
        // Required empty public constructor
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //We need to cast the activity to AppCompatActivity because the get getSupportActionBar() exists in there
        //Also we need to check for null, because an activity may not have an actionbar
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        ActionBar actionBar = null;
        if (activity != null) {
            actionBar = activity.getSupportActionBar();
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.menumain_delete_generated).setVisible(false);
        menu.findItem(R.id.menumain_generate).setVisible(false);
        menu.findItem(R.id.menumain_showDurations).setVisible(false);
        menu.findItem(R.id.menumain_settings).setVisible(false);
        menu.findItem(R.id.menumain_showAbout).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        mNameTextView = view.findViewById(R.id.addedit_name);
        mDescriptionTextView = view.findViewById(R.id.addedit_description);
        mSortOrderTextView = view.findViewById(R.id.addedit_sortorder);
        Button saveButton = view.findViewById(R.id.addedit_btn_save);

        mViewModelTask = new ViewModelProvider(getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(ViewModelTask.class);


        final Task taskToEdit = mViewModelTask.getSelectedTaskToEdit().getValue();
        if (taskToEdit != null) {
            Log.d(TAG, "onCreateView: the task details found, editing...");
            mNameTextView.setText(taskToEdit.getName());
            mDescriptionTextView.setText(taskToEdit.getDescription());
            mSortOrderTextView.setText(Integer.toString(taskToEdit.getSortOrder()));
            mMode = FragmentEditMode.EDIT;
        } else {
            Log.d(TAG, "onCreateView: no arguments, adding a new record");
            mMode = FragmentEditMode.ADD;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the database if at least one field has changed.
                //- There is no reason to hit the database unless this has happened.
                int so; //to save repeated conversions to int.
                if (mSortOrderTextView.length() > 0) {
                    so = Integer.parseInt(mSortOrderTextView.getText().toString());
                } else {
                    so = 0;
                }

                String name;
                String description;
                int sortOrder;
                switch (mMode) {
                    case ADD:
                        if (mNameTextView.length() > 0) {
                            Log.d(TAG, "onClick: adding new task");
                            name = mNameTextView.getText().toString();
                            description = mDescriptionTextView.getText().toString();
                            sortOrder = so;
                            Task task = new Task(name, description, sortOrder);
                            task.setRunning(false);
                            mViewModelTask.insert(task);
                        }
                        break;
                    case EDIT:

                        if (!mNameTextView.getText().toString().equalsIgnoreCase(taskToEdit.getName())) {
                            name = mNameTextView.getText().toString();
                        }else {
                            name = taskToEdit.getName();
                        }
                        if (!mDescriptionTextView.getText().toString().equalsIgnoreCase(taskToEdit.getDescription())) {
                            description = mDescriptionTextView.getText().toString();
                        }else {
                            description = taskToEdit.getDescription();
                        }
                        if (so != taskToEdit.getSortOrder()) {
                            sortOrder = so;
                        }else {
                            sortOrder = taskToEdit.getSortOrder();
                        }
                        if (!name.equalsIgnoreCase(taskToEdit.getName()) || !description.equalsIgnoreCase(taskToEdit.getDescription())
                                || sortOrder != taskToEdit.getSortOrder()) {
                            Log.d(TAG, "onClick: updating task");
                            Task task = new Task(name, description, sortOrder);
                            boolean running = taskToEdit.isRunning();
                            long id = taskToEdit.getId();
                            task.setRunning(running);
                            task.setId(id);
                            mViewModelTask.update(task);
                        }

                        break;
                }

                Log.d(TAG, "onClick: done editing");
                mViewModelTask.selectTaskToEdit(null);
                ((MainActivity)getActivity()).onSaveClicked();
                mCanCloseWhenBackBtnPrssd = true;
            }
        });
        Log.d(TAG, "onCreateView: ends");
        return view;
    }

    boolean canClose() {
        return mCanCloseWhenBackBtnPrssd;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        ActionBar actionBar = null;
        if (activity != null) {
            actionBar = activity.getSupportActionBar();
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }
}
