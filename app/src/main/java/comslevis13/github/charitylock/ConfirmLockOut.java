package comslevis13.github.charitylock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by slevi on 11/27/2017.
 */

public class ConfirmLockOut extends android.support.v4.app.DialogFragment {

    public static ConfirmLockOut newInstance() {
        ConfirmLockOut confirmLockOut = new ConfirmLockOut();
        return confirmLockOut;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).onDialogPositiveClick();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).onDialogNegativeClick();
                            }
                        }
                )
                .create();
    }
}
