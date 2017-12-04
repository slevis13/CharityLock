package comslevis13.github.charitylock;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by slevi on 11/30/2017.
 */

public class ConfirmDonation extends android.support.v4.app.DialogFragment {

    public static ConfirmDonation newInstance() {
        ConfirmDonation confirmDonation = new ConfirmDonation();
        return confirmDonation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("Donate",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((PersistActivity)getActivity()).onDialogPositiveClick();
                            }
                        }
                )
                .setNegativeButton("Nvm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((PersistActivity)getActivity()).onDialogNegativeClick();
                            }
                        }
                )
                .create();
    }
}
