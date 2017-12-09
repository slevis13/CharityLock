package comslevis13.github.worklock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by slevi on 11/27/2017.
 */

public class DialogConfirm extends android.support.v4.app.DialogFragment {

    private int hoursToLock;
    private int minutesToLock;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle getBundle = getArguments();
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
}
