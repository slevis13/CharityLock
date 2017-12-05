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

    // TODO: time selector UI
    // TODO: what happens when device rotates?
    // TODO: what happens when user gets a phone call, text, other notification?
    // TODO; if user doesn't have wifi/data? could generate json object, send when wifi detected

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

    // get time from time picker
    private void getTime () {
        // get input from editText, store in timeToLock
        hoursToLock = hourPicker.getValue();
        minutesToLock = minutePicker.getValue();
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
    }

    public void onDialogNegativeClick() {
        // User touched the dialog's negative button
    }
}
