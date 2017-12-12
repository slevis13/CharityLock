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

    private TelephonyManager mTelephonyManager;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

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
//                stopPersistService();
//
//
//                Intent whiteListService = new Intent(getApplicationContext(), WhiteListService.class);
//                startService(whiteListService);
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
            if (isPhonePermissionEnabled()) {
                // todo: handle emergency calls
                // todo: set listener to return to activity after call
                // todo: dialing UI
                makePhoneCall();
                Log.d("phone permiss enabled", "got it -- ya boy");

            } else {
                Toast.makeText(this, "Calling permission not granted",
                        Toast.LENGTH_LONG).show();
            }
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

    @SuppressLint("MissingPermission")
    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:3046203109"));
        if (isPhonePermissionEnabled()) {
            startActivity(intent);
        }
    }

//    private boolean isTelephonyEnabled(TelephonyManager telephonyManager) {
//        if (telephonyManager != null) {
//            if (telephonyManager.getSimState() ==
//                    TelephonyManager.SIM_STATE_READY) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void checkForPhonePermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
//                PackageManager.PERMISSION_GRANTED) {
//            stopPersistService();
//            // Permission not yet granted. Use requestPermissions().
//            Log.d("permission", "permission not granted -- ya boy");
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                // permission was granted, yay! Do the
//                // contacts-related task you need to do.
//
//            } else {
//                startPersistService();
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//            }
//            return;
//        }
//
//    }

    private void startCountDown(long millisUntilFinished, long countDownInterval) {

        // launch persist service
        startPersistService();

        // launch countdown, display time
        new CountDownTimer(millisUntilFinished, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                handleOnTick(millisUntilFinished);
            }
            // finished
            public void onFinish() {
                unlockAndFinish();
                sendDoneNotification();
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

    private void unlockAndFinish() {
        timeLeftTitle.setText(getString(R.string.persist_text_on_finish));
        millsLeft = 0;

        stopPersistService();

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
