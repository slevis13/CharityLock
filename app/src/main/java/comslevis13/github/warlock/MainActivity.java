package comslevis13.github.warlock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int hoursToLock;
    private int minutesToLock;

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;

    private Button lockButton;

    protected static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    protected TelephonyManager mTelephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

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
        lockButton = (Button) findViewById(R.id.button_time_picker);
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

    // handled as own method in case other functionality needs to be added to button click
    private void handleLockButtonClick () {
        checkTelephonyAndPermissions();
    }

    private void checkTelephonyAndPermissions() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // if device is a tablet, do not ask for permissions
        if (!isDeviceAPhone()) {
            getTimeAndLaunchDialog();
            return;
        }
        // check permission; if not granted, request it
        if (isPhonePermissionEnabled()) {
            getTimeAndLaunchDialog();
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    private boolean isDeviceAPhone() {
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null &&
                telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    private boolean isPhonePermissionEnabled() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                getTimeAndLaunchDialog();
            } else {
                // permission denied
            }
        }
    }

    private void getTimeAndLaunchDialog() {
        getTime();
        if (isTimeNonzero(hoursToLock, minutesToLock)) {
            launchDialogConfirm();
        }
        else {
            Toast.makeText(this, "Set a time!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isTimeNonzero(int hrs, int mins) {
        return hrs + mins > 0;
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

    private long hoursAndMinutesToMilliseconds(int hoursArg, int minutesArg) {
        return (long) (hoursArg * 3600000) + (minutesArg * 60000);
    }

    // callbacks for dialog button clicks

    public void onDialogPositiveClick() {
        // User touched the dialog's positive button
        // pass time to persistactivity
        Intent intent = new Intent(this, PersistActivity.class);
        long millsToLock = hoursAndMinutesToMilliseconds(hoursToLock, minutesToLock);
        intent.putExtra(getString(R.string.dialog_intent_mills), millsToLock);

        startActivity(intent);
        finish();
    }

    public void onDialogNegativeClick() {
        // User touched the dialog's negative button
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save state of hourPicker and minutePicker
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.hour_picker_save_key), getHours());
        outState.putInt(getString(R.string.minute_picker_save_key), getMinutes());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lockButton.setOnClickListener(null);
    }
}
