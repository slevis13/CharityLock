package comslevis13.github.warlock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by slevi on 11/27/2017.
 */

public class PersistActivity extends FragmentActivity {

    private int COUNTDOWN_INTERVAL = 100;
    private long MILLISECONDS_IN_HOUR = 3600000;
    private long MILLISECONDS_IN_MINUTE = 60000;
    private long MILLISECONDS_IN_SECOND = 1000;

    private FragmentManager supportFragmentManager;

    private TextView hoursLeftTextView;
    private TextView minutesLeftTextView;
    private TextView secondsLeftTextView;
    private TextView timeLeftTitle;
    private Button mDialButton;

    private long hrs;
    private long mins;
    private long secs;
    private long millsLeft;
    private CountDownTimer mCountdownTimer;

    private TelephonyManager mTelephonyManager;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private String phoneNumber = "911";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.persist_activity);

        if (savedInstanceState == null) {
            // get passed-in timeToLock from intent
            Intent intent = getIntent();
            millsLeft = intent.getLongExtra(getString(R.string.dialog_intent_mills), 0);
        } else {
            // restore time left
            long mSavedTimeInMilliseconds = savedInstanceState.getLong(
                    getString(R.string.persist_mills_current_save_key));
            long mSavedMillsLeft = savedInstanceState.getLong(
                    getString(R.string.persist_mills_left_save_key));
            long currentTimeInMilliseconds = System.currentTimeMillis();

            millsLeft = mSavedMillsLeft - (currentTimeInMilliseconds - mSavedTimeInMilliseconds);
        }

        // set text view variables
        hoursLeftTextView = (TextView) findViewById(R.id.text_view_hours_left_persist);
        minutesLeftTextView = (TextView) findViewById(R.id.text_view_minutes_left_persist);
        secondsLeftTextView = (TextView) findViewById(R.id.text_view_seconds_left_persist);
        timeLeftTitle = (TextView) findViewById(R.id.time_left_title);

        mDialButton = (Button) findViewById(R.id.button);
        mDialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonPress();
            }
        });

        // start countdown and lock user into app
        startCountDown(millsLeft, COUNTDOWN_INTERVAL);
    }

    private void handleButtonPress() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        // check that telephony enabled on device
        if (MainActivity.isTelephonyEnabled(mTelephonyManager)) {
            // check permission; if not granted, request it
            Log.d("telephony", "telephony enabled -- ya boy");
            // todo: test handling of emergency calls
            // todo: set listener to return to activity after call
            // todo: UI to input #
            handlePhoneCall(phoneNumber);
        }
        // if telephony disabled, disable call button
        else {
            Log.d("telephony", "telephony not enabled -- ya boy");
            Toast.makeText(this, "Unable to make calls on this device",
                    Toast.LENGTH_LONG).show();
            mDialButton.setOnClickListener(null);
        }
    }

    private boolean isPhonePermissionEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // permission not yet granted
            Toast.makeText(this, "Phone permission not enabled!",
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void handlePhoneCall(String phoneNumber) {
        if (PhoneNumberUtils.isEmergencyNumber(phoneNumber)) {
            // stop the lock if user dials an emergency number
            stopPersistService();
            // dial emergency number in dialer
            Uri emergencyNumber = Uri.parse("tel:" + phoneNumber);
            Intent emergencyIntent = new Intent(Intent.ACTION_DIAL, emergencyNumber);
            startActivity(emergencyIntent);
            // stop lock and countdown, update notification
            stopCountdownAndSendDoneNotification();
        }
        else {
            // not emergency number
        }
    }

    @SuppressLint("MissingPermission")
    private void makePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (isPhonePermissionEnabled()) {
            // todo: set listener to retrun to lock after call
            startActivity(callIntent);
        }

    }

    private void startCountDown(long millisUntilFinished, long countDownInterval) {
        // launch persist service
        startPersistService();
        // launch countdown, display time
        mCountdownTimer = new CountDownTimer(millisUntilFinished, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                handleOnTick(millisUntilFinished);
            }
            // finished
            public void onFinish() {
                unlockAndFinish();
            }
        }.start();
    }

    private void handleOnTick(long milliseconds) {
        // generate values for time left
        hrs = milliseconds / MILLISECONDS_IN_HOUR;
        mins = (milliseconds % MILLISECONDS_IN_HOUR) / MILLISECONDS_IN_MINUTE;
        secs = ((milliseconds % MILLISECONDS_IN_HOUR)
                % MILLISECONDS_IN_MINUTE) / MILLISECONDS_IN_SECOND;
        // display values
        hoursLeftTextView.setText(Long.toString(hrs));
        minutesLeftTextView.setText(Long.toString(mins));
        secondsLeftTextView.setText(Long.toString(secs));

        // update global variable
        millsLeft = milliseconds;

        updateNotification();
    }

    private void stopCountdownAndSendDoneNotification() {
        mCountdownTimer.cancel();
        millsLeft = 0;
        stopPersistService();
        sendDoneNotification();
    }

    private void unlockAndFinish() {
        stopCountdownAndSendDoneNotification();
        timeLeftTitle.setText(getString(R.string.persist_text_on_finish));
        // bring user back to main screen
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void startPersistService() {
        Intent persistService = new Intent(this, PersistService.class);
        startService(persistService);
    }
    private void stopPersistService() {
        // stop PersistService (i.e. unlock user from app)
        Intent persistService = new Intent(getApplicationContext(), PersistService.class);
        stopService(persistService);
    }

    private void updateNotification() {
        String notificationMessageString =
                Long.toString(hrs) + ": " + Long.toString(mins) + ": " + Long.toString(secs);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // get notification object
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(notificationMessageString)
                        .setPriority(NotificationCompat.PRIORITY_LOW);
        // fire notification
        mNotificationManager.notify(001, mBuilder.build());
    }

    private void sendDoneNotification() {
        String doneNotificationMessage = "Done!";
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon_unlock)
                        .setContentTitle("Unlocked")
                        .setContentText(doneNotificationMessage)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mNotificationManager.notify(001, mBuilder.build());
    }

    @Override
    public void onBackPressed() {
        // disable back button
    }

    @Override
    protected void onPause() {
        storeTimesInSharedPreferences();
        super.onPause();
    }

    // update sharedPreferences with timeLeft and currentTime, in milliseconds
    private void storeTimesInSharedPreferences() {
        SharedPreferences settings = getSharedPreferences(
                getString(R.string.shared_prefs_file_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(getString(R.string.shared_prefs_milliseconds_saved), millsLeft);
        editor.putLong(getString(R.string.shared_prefs_time_at_shutdown),
                System.currentTimeMillis());

        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save mills left and current time in mills
        outState.putLong(getString(R.string.persist_mills_left_save_key), millsLeft);
        outState.putLong(getString(R.string.persist_mills_current_save_key),
                System.currentTimeMillis());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        // stop persistService
        Intent persistService = new Intent(getApplicationContext(), PersistService.class);
        stopService(persistService);

        super.onDestroy();
    }
}
