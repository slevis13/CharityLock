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
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends FragmentActivity {

    private int hoursToLock;
    private int minutesToLock;

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        setPickerRanges();

        if (savedInstanceState != null) {
            // restore saved state of number pickers
            final int mHoursSaved = savedInstanceState.getInt(
                    getString(R.string.hour_picker_save_key), 0);
            final int mMinutesSaved = savedInstanceState.getInt(
                    getString(R.string.minute_picker_save_key), 0);

            hourPicker.setValue(mHoursSaved);
            minutePicker.setValue(mMinutesSaved);
        }

        // listen for button click
        final Button lockButton = (Button) findViewById(R.id.button_time_picker);
        lockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleLockButtonClick();
            }
        });
    }

    // initialize number pickers
    private void setPickerRanges () {
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
    }

    // get time and launch confirmation dialog
    private void handleLockButtonClick () {
        getTime();
        launchDialogConfirm();
    }

    private int getHours() {
        return hourPicker.getValue();
    }

    private int getMinutes() {
        return minutePicker.getValue();
    }
    // get time from time picker
    private void getTime () {
        // get input from editText, store in timeToLock
        hoursToLock = getHours();
        minutesToLock = getMinutes();
    }

    private void launchDialogConfirm () {
        DialogConfirm dialogConfirm = new DialogConfirm();

        Bundle timeBundle = new Bundle();
        timeBundle.putInt(getString(R.string.dialog_intent_hours), hoursToLock);
        timeBundle.putInt(getString(R.string.dialog_intent_minutes), minutesToLock);
        dialogConfirm.setArguments(timeBundle);

        dialogConfirm.show(getSupportFragmentManager(), "launchDialog");
    }

    // callbacks for dialog button clicks

    public void onDialogPositiveClick() {
        // User touched the dialog's positive button
        // pass hours, minutes to persistactivity
        Intent intent = new Intent(this, PersistActivity.class);
        intent.putExtra(getString(R.string.dialog_intent_hours), hoursToLock);
        intent.putExtra(getString(R.string.dialog_intent_minutes), minutesToLock);
        startActivity(intent);
        finish();
    }

    public void onDialogNegativeClick() {
        // User touched the dialog's negative button
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save state of hourPicker and minutePicker
        outState.putInt(getString(R.string.hour_picker_save_key), getHours());
        outState.putInt(getString(R.string.minute_picker_save_key), getMinutes());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        findViewById(R.id.button_time_picker).setOnClickListener(null);
        super.onDestroy();
    }
}
