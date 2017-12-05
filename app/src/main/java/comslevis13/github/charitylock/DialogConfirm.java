package comslevis13.github.charitylock;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import java.util.logging.Logger;

/**
 * Created by slevi on 11/27/2017.
 */

public class DialogConfirm extends android.support.v4.app.DialogFragment {

    private int hoursToLock;
    private int minutesToLock;

    public static DialogConfirm newInstance() {
        DialogConfirm dialogConfirm = new DialogConfirm();
        return dialogConfirm;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle getBundle = getArguments();
        if (getBundle == null) {
            Log.e("bundle null", "bundle null");
        }

        // get time vals from bundle
        hoursToLock = getBundle.getInt(getString(R.string.dialog_intent_hours),
                0);
        minutesToLock = getBundle.getInt(getString(R.string.dialog_intent_minutes),
                0);

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_confirm_title))
                .setMessage(buildConfirmDialogMessage())
                .setPositiveButton(getString(R.string.confirm_dialog_yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).onDialogPositiveClick();
                            }
                        }
                )
                .setNegativeButton(getString(R.string.confirm_dialog_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).onDialogNegativeClick();
                            }
                        }
                )
                .create();
    }

    private String buildConfirmDialogMessage () {
        String message = "\n" + getString(R.string.dialog_confirm_message) +
                String.valueOf(hoursToLock) + " hours, " +
                String.valueOf(minutesToLock) + " minutes \n";
        return message;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
//    public interface NoticeDialogListener {
//        public void onDialogPositiveClick(DialogFragment dialog);
//        public void onDialogNegativeClick(DialogFragment dialog);
//    }
//
//    // Use this instance of the interface to deliver action events
//    NoticeDialogListener mListener;
//
//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (NoticeDialogListener) context;
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(context.toString()
//                    + " must implement NoticeDialogListener");
//        }
//    }



    // called by show() in Main; creates dialog and sets onClick listener for each
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage("Are you sure?")
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // 'YES' clicked. Launch PersistActivity
//
//
////                          Intent intent = new Intent(getActivity(), PersistActivity.class);
////                        startActivity(intent);
//                    }
//                })
//                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // 'Cancel' clicked. Return to parent
//
//                    }
//                });
//        // Create the AlertDialog object and return it
//        return builder.create();
//    }
}
