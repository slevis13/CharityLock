package comslevis13.github.charitylock;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends FragmentActivity
        implements DialogConfirm.NoticeDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // gets time from time picker, on 'Done' button click
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getTime (View view) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        int timePickerHour = timePicker.getHour();
        int timePickerMinute = timePicker.getMinute();

        launchDialogConfirm(timePickerHour, timePickerMinute);
    }

    public void launchDialogConfirm (int hour, int minute) {
        DialogConfirm dialogConfirm = new DialogConfirm();
        dialogConfirm.show(getSupportFragmentManager(), "launchDialog");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        TextView displayTime = (TextView) findViewById(R.id.time_display);
        displayTime.setText("yeet");

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }
}
