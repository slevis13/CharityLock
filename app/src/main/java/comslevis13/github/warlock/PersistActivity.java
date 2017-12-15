package comslevis13.github.warlock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by slevi on 11/27/2017.
 */

public class PersistActivity extends FragmentActivity
        implements MakeCallButtonFragment.OnButtonPressedListener{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private String phoneNumber = "3046203109";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.persist_activity);

        if (findViewById(R.id.frameLayoutForCallElements) != null) {
            if (savedInstanceState == null) {
                MakeCallButtonFragment buttonFragment = new MakeCallButtonFragment();
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayoutForCallElements, buttonFragment).commit();
            }
        }

//        mDialButton = (Button) findViewById(R.id.button);
//        mDialButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                handleButtonPress();
//            }
//        });
//        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // start countdown and lock user into app
//        startCountDown(millsLeft, COUNTDOWN_INTERVAL);
        Log.d("persist onCreate", "onCreate -- ya boy");

//        Intent listenerIntent = new Intent(this, ListenerService.class);
//        startService(listenerIntent);

    }

    @Override
    public void onBackPressed() {
        // disable back button
    }

    @Override
    public void onButtonPressed(int flag) {
        //
        Toast.makeText(this, "got em", Toast.LENGTH_LONG).show();
    }



//    @Override
//    protected int getLayoutResourceId() {
//        return R.layout.persist_activity;
//    }

//    private void handleButtonPress() {
//
//        // todo: launch new activity with altered layout
//        // check that telephony enabled on device
//        if (isTelephonyEnabled(mTelephonyManager)) {
//            // check permission; if not granted, request it
//            Log.d("button press", "button press -- ya boy");
//            stopPersistService();
//            Intent intent = new Intent(this, PersistCallActivity.class);
//            startActivity(intent);
//        }
//        // if telephony disabled, disable call button
//        else {
//            Log.d("telephony", "telephony not enabled -- ya boy");
//            Toast.makeText(this, "Unable to make calls on this device",
//                    Toast.LENGTH_LONG).show();
//            mDialButton.setOnClickListener(null);
//        }
//    }
//
//    private boolean isTelephonyEnabled(TelephonyManager telephonyManager) {
//        if (telephonyManager != null) {
//            if (telephonyManager.getSimState() ==
//                    TelephonyManager.SIM_STATE_READY) {
//                // device has telephony enabled
//                return true;
//            }
//        }
//        return false;
//    }

//    private void startCountDown(long millisUntilFinished, long countDownInterval) {
//        // launch persist service
//        startPersistService();
//        // launch countdown, display time
//        mCountdownTimer = new CountDownTimer(millisUntilFinished, countDownInterval) {
//            public void onTick(long millisUntilFinished) {
//                handleOnTick(millisUntilFinished);
//            }
//            // finished
//            public void onFinish() {
//                unlockAndFinish();
//            }
//        }.start();
//    }
//
//    private void handleOnTick(long milliseconds) {
//        // generate values for time left
//        hrs = milliseconds / MILLISECONDS_IN_HOUR;
//        mins = (milliseconds % MILLISECONDS_IN_HOUR) / MILLISECONDS_IN_MINUTE;
//        secs = ((milliseconds % MILLISECONDS_IN_HOUR)
//                % MILLISECONDS_IN_MINUTE) / MILLISECONDS_IN_SECOND;
//        // display values
//        hoursLeftTextView.setText(Long.toString(hrs));
//        minutesLeftTextView.setText(Long.toString(mins));
//        secondsLeftTextView.setText(Long.toString(secs));
//
//        // update global variable
//        millsLeft = milliseconds;
//
//        updateNotification();
//    }
//
//    protected void stopCountdownAndSendDoneNotification(CountDownTimer countDownTimer) {
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//        millsLeft = 0;
//        stopPersistService();
//        sendDoneNotification();
//    }
//
//    private void unlockAndFinish() {
//        stopCountdownAndSendDoneNotification(mCountdownTimer);
//        stopListenerService();
//        timeLeftTitle.setText(getString(R.string.persist_text_on_finish));
//        // bring user back to main screen
//        Intent mainActivity = new Intent(this, MainActivity.class);
//        startActivity(mainActivity);
//        finish();
//    }
//
//    private void startPersistService() {
//        Intent persistService = new Intent(this, PersistService.class);
//        startService(persistService);
//    }
//    protected void stopPersistService() {
//        // stop PersistService (i.e. unlock user from app)
//        Intent persistService = new Intent(this, PersistService.class);
//        stopService(persistService);
//    }
//
//    private void stopListenerService() {
//        Intent listenerService = new Intent(this, ListenerService.class);
//        stopService(listenerService);
//
//    }
//
//    private void updateNotification() {
//        String notificationMessageString =
//                Long.toString(hrs) + ": " + Long.toString(mins) + ": " + Long.toString(secs);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        // get notification object
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.notification_icon)
//                        .setContentTitle(getString(R.string.notification_title))
//                        .setContentText(notificationMessageString)
//                        .setPriority(NotificationCompat.PRIORITY_LOW);
//        // fire notification
//        if (mNotificationManager != null) {
//            mNotificationManager.notify(001, mBuilder.build());
//        }
//    }
//
//    private void sendDoneNotification() {
//        String doneNotificationMessage = "Done!";
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.notification_icon_unlock)
//                        .setContentTitle("Unlocked")
//                        .setContentText(doneNotificationMessage)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        if (mNotificationManager != null) {
//            mNotificationManager.notify(001, mBuilder.build());
//        }
//    }
//

//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        storeTimesInSharedPreferences();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    // update sharedPreferences with timeLeft and currentTime, in milliseconds
//    private void storeTimesInSharedPreferences() {
//        SharedPreferences settings = getSharedPreferences(
//                getString(R.string.shared_prefs_file_name), 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putLong(getString(R.string.shared_prefs_milliseconds_saved), millsLeft);
//        editor.putLong(getString(R.string.shared_prefs_time_at_shutdown),
//                System.currentTimeMillis());
//
//        editor.apply();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        // save mills left and current time in mills
//        outState.putLong(getString(R.string.persist_mills_left_save_key), millsLeft);
//        outState.putLong(getString(R.string.persist_mills_current_save_key),
//                System.currentTimeMillis());
//
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // stop persistService
////        stopPersistService();
////        Intent stopListenerService = new Intent(this, ListenerService.class);
////        stopService(stopListenerService);
//        Log.d("persist onDestroy", "onDestroy -- ya boy");
//
//    }
}
