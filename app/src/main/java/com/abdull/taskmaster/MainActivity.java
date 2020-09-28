package com.abdull.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abdull.taskmaster.models.Task;
import com.abdull.taskmaster.viewmodels.ViewModelTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements AppDialog.DialogEvents {

    private static final String TAG = "MainActivity";

    private AlertDialog mDialogAbout = null; //module scope because we need to dismiss it in onStop
    private ViewModelTask mViewModelTask;

    public static final int DIALOG_ID_DELETE = 1;
    public static final int DIALOG_ID_UP_BTN_CANCEL_EDIT = 2;
    public static final int DIALOG_ID_BACK_BTN_CANCEL_EDIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //To attach the viewmodel lifecycle to the activity(owner) the viewmodel will be destroyed once the activity is finished
        mViewModelTask = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ViewModelTask.class);

        FragmentManager fragmentManager = getSupportFragmentManager();

        boolean editing = fragmentManager.findFragmentById(R.id.task_details_container) != null;

        View mainActFragmentView = findViewById(R.id.fragment);
        View addEditFragmentView = findViewById(R.id.task_details_container);

        if (editing) {
            Log.d(TAG, "onCreate: single-pane mode editing");
            // hide the left hand fragment, to make room for editing
            mainActFragmentView.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "onCreate: single-pane mode, not editing");
            addEditFragmentView.setVisibility(View.GONE);
            mainActFragmentView.setVisibility(View.VISIBLE);
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_task);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskEditRequest();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (BuildConfig.DEBUG) {
            MenuItem generate = menu.findItem(R.id.menumain_generate);
            generate.setVisible(true);
            MenuItem deleteGenerate = menu.findItem(R.id.menumain_delete_generated);
            deleteGenerate.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected: starts");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.menumain_showDurations:
                /*Intent intent = new Intent(this, DurationsReportActivity.class);
                startActivity(intent);*/
                return true;
            case R.id.menumain_settings:
                return true;

            case R.id.menumain_showAbout:
                showAboutDialog();
                return true;

            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: home btn pressed");
                AddEditFragment addEditFragment = (AddEditFragment)
                        getSupportFragmentManager().findFragmentById(R.id.task_details_container);
                if (addEditFragment.canClose()) {
                    removeAddEditFragment();
                    return true;
                } else {
                    editBackBtnPressedDialog(DIALOG_ID_UP_BTN_CANCEL_EDIT);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void showAboutDialog() {

        //We need to dismiss it on the onStop method to avoid memory leaks
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);

        builder.setView(messageView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.d(TAG, "onClick: entering messageView.onClick dialog is showing " + mDialogAbout.isShowing());
                if (mDialogAbout != null && mDialogAbout.isShowing()) {
                    mDialogAbout.dismiss();
                }

            }
        });

        mDialogAbout = builder.create();
        mDialogAbout.setCanceledOnTouchOutside(true);


        TextView tvVersion = messageView.findViewById(R.id.about_version);
        tvVersion.setText("v" + BuildConfig.VERSION_NAME);

        TextView tvUrl = messageView.findViewById(R.id.about_web_url);
        if (tvUrl != null) {
            Log.d(TAG, "showAboutDialog: current android version is " + Build.VERSION.SDK_INT);
            tvUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = ((TextView) view).getText().toString();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "No browser found to launch URL, or URL is invalid", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            Log.d(TAG, "showAboutDialog: current android version is " + Build.VERSION.SDK_INT);
        }


        mDialogAbout.show();

    }

    private void taskEditRequest() {
        Log.d(TAG, "taskEditRequest: starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditFragment addEditFragment = new AddEditFragment();
        fragmentManager.beginTransaction().replace(R.id.task_details_container, addEditFragment).commit();


        // Hide the left hand fragment and show the right hand frame
        View mainFragView = findViewById(R.id.fragment);
        View addEditFragView = findViewById(R.id.task_details_container);
        mainFragView.setVisibility(View.GONE);
        addEditFragView.setVisibility(View.VISIBLE);
        findViewById(R.id.fab_task).setVisibility(View.GONE);
        findViewById(R.id.bottom_nav).setVisibility(View.GONE);

            /*// in single-pane mode, start the detail activity for the selected item id.
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if(task != null) {//Editing a task
                Log.d(TAG, "taskEditRequest: single-pane mode task != null editing... ");
                detailIntent.putExtra(Task.class.getSimpleName(), task);
//                startActivity(detailIntent);
            }
//            else{
//                Log.d(TAG, "taskEditRequest: task is null adding a new one");
//                // adding a new task
////                startActivity(detailIntent);
//            }
            startActivity(detailIntent); // since we are calling the startActivity anyway, we call it once (extract common part from if)*/

    }


    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment addEditFragment = fragmentManager.findFragmentById(R.id.task_details_container);

        fragmentManager.beginTransaction().remove(addEditFragment).commit();

        //We removed the fragment but we need to show the mainActFrag view and hide the addedit view
        View mainFragView = findViewById(R.id.fragment);
        View addEditFragView = findViewById(R.id.task_details_container);
        addEditFragView.setVisibility(View.GONE);
        mainFragView.setVisibility(View.VISIBLE);
        findViewById(R.id.fab_task).setVisibility(View.VISIBLE);
        findViewById(R.id.bottom_nav).setVisibility(View.VISIBLE);

    }


    public void goToEditFragment() {
        taskEditRequest();
    }


    public void onDeleteClick(@NonNull Task task) {
        mViewModelTask.selectTaskToDelete(task);
        AppDialog appDialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DIALOG_ID_DELETE);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message, task.getId(), task.getName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);
        appDialog.setArguments(args);
        appDialog.show(getSupportFragmentManager(), null);
    }

    public void showSingleTaskReport(Task task){

    }

    @Override
    public void onPositiveDialogResult(int dialogID, Bundle args) {
        switch (dialogID) {
            case DIALOG_ID_DELETE:
                Task task = mViewModelTask.getSelectedTaskToDelete().getValue();
                if (task != null) {
                    mViewModelTask.delete(task);
                    mViewModelTask.selectTaskToDelete(null);

                }
                break;

            case DIALOG_ID_BACK_BTN_CANCEL_EDIT:
            case DIALOG_ID_UP_BTN_CANCEL_EDIT:
                mViewModelTask.selectTaskToDelete(null);
                mViewModelTask.selectTaskToEdit(null);
                //if we are editing remove the fragment otherwise close the app
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment addEditFragment = fragmentManager.findFragmentById(R.id.task_details_container);
                if (addEditFragment != null) {

                    removeAddEditFragment();

                } else {

                    finish();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown dialog id " + dialogID);
        }
    }

    @Override
    public void onNegativeDialogResult(int dialogID, Bundle args) {

        switch (dialogID) {
            case DIALOG_ID_DELETE:
                mViewModelTask.selectTaskToDelete(null);
                break;
        }
    }

    @Override
    public void onDialogCancelled(int dialogID) {
        switch (dialogID) {
            case DIALOG_ID_DELETE:
                mViewModelTask.selectTaskToDelete(null);
                break;
        }
    }

    private void removeAddEditFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditFragment addEditFragment = (AddEditFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if(addEditFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(addEditFragment).commit();
            findViewById(R.id.fab_task).setVisibility(View.VISIBLE);
            findViewById(R.id.bottom_nav).setVisibility(View.VISIBLE);
            View mainFragView = findViewById(R.id.fragment);
            View addEditFragView = findViewById(R.id.task_details_container);
            mainFragView.setVisibility(View.VISIBLE);
            addEditFragView.setVisibility(View.GONE);
        }
    }

    private void editBackBtnPressedDialog(int dialogID) {
        //Show dialog to get confirmation to quit editing
        AppDialog appDialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, dialogID);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.extdiag_message));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.extdiag_positive_caption);
        args.putInt(AppDialog.DIALOG_NEGATIVE_RID, R.string.cancel);
        appDialog.setArguments(args);
        appDialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditFragment addEditFragment = (AddEditFragment) fragmentManager.findFragmentById(R.id.task_details_container);
        if(addEditFragment == null){
            mViewModelTask.selectTaskToDelete(null);
            mViewModelTask.selectTaskToEdit(null);
            super.onBackPressed();
        }
        else if (addEditFragment.canClose()) {
            mViewModelTask.selectTaskToDelete(null);
            mViewModelTask.selectTaskToEdit(null);
            removeAddEditFragment();
        } else {
            editBackBtnPressedDialog(DIALOG_ID_BACK_BTN_CANCEL_EDIT);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: starts");
        super.onStop();

        if (mDialogAbout != null && mDialogAbout.isShowing()) {
            mDialogAbout.dismiss();
        }
    }
}