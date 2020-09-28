package com.abdull.taskmaster;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Created by Abdullah Alqahtani on 9/23/2020.
 */
public class AppDialog extends DialogFragment {
    private static final String TAG = "AppDialog";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";

    /**
     *  The dialog's callback interface to notify of user selected results (deletion confirmed, etc.)
     */
    interface DialogEvents{
        void onPositiveDialogResult(int dialogID, Bundle args);
        void onNegativeDialogResult(int dialogID, Bundle args);
        void onDialogCancelled(int dialogID);
    }

    private DialogEvents mDialogEvents;

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        //Activities containing this fragment must implement DialogEvents callback interface

        if(!(context instanceof DialogEvents)){
            throw new ClassCastException(context.toString() + " Must implement AppDialog.DialogEvents interface.");
        }

        mDialogEvents = (DialogEvents) context;
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: starts");

        final Bundle arguments = getArguments();
        final int dialogID;
        //We made the message type a string, instead of an int which provides the message from the string resources file
        //is because we might add extra text coming from i.e. task.getName and append it to the message
        String messageString;
        int positiveStringID;
        int negativeStringID;

        if(arguments != null){
            dialogID = arguments.getInt(DIALOG_ID);
            messageString = arguments.getString(DIALOG_MESSAGE);

            if(dialogID == 0 || messageString == null){
                throw new IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE not present in the bundle");
            }
            positiveStringID = arguments.getInt(DIALOG_POSITIVE_RID);
            if(positiveStringID == 0){
                positiveStringID = R.string.ok;
            }
            negativeStringID = arguments.getInt(DIALOG_NEGATIVE_RID);
            if(negativeStringID == 0){
                negativeStringID = R.string.cancel;
            }
        }else{
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle.");
        }




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(messageString).setPositiveButton(positiveStringID, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                // callback positive result method.
                if(mDialogEvents != null){
                    mDialogEvents.onPositiveDialogResult(dialogID, arguments);
                }

            }
        }).setNegativeButton(negativeStringID, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                // callback negative result method
                if(mDialogEvents != null) {
                    mDialogEvents.onNegativeDialogResult(dialogID, arguments);
                }
            }
        });
        Log.d(TAG, "onCreateDialog: returning...");
        return builder.create();
    }



    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        Log.d(TAG, "onCancel: starts");
        if(mDialogEvents != null){
            int dialogID = getArguments().getInt(DIALOG_ID);
            mDialogEvents.onDialogCancelled(dialogID);
        }

    }



    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        //Reset the callback interface, because we don't have an activity anymore
        mDialogEvents = null;
    }
}
