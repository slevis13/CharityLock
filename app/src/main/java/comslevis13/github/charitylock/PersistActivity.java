package comslevis13.github.charitylock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by slevi on 11/27/2017.
 */

public class PersistActivity extends FragmentActivity {

    private int hoursLocked;
    private int minutesLocked;

    private int COUNTDOWN_INTERVAL = 100;
    private FragmentManager supportFragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.persist_activity);

        // get passed-in timeToLock from intent
        Intent intent = getIntent();
        hoursLocked = intent.getIntExtra(getString(R.string.dialog_intent_hours), 0);
        minutesLocked = intent.getIntExtra(getString(R.string.dialog_intent_minutes), 0);


        // generate milliseconds lock value
        int secondsLocked = minutesLocked * 60 + hoursLocked * 3600;
        int millsLocked = secondsLocked * 1000;

        // start countdown and lock user into app
        startCountDown(millsLocked, COUNTDOWN_INTERVAL);
    }

//    @Override
//    protected void onPause() {
//
//        if(!MyActivityLifecycleCallbacks.isAppInForeground()) {
//            Intent forceToTop = new Intent(getApplicationContext(), PersistActivity.class);
//            forceToTop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(forceToTop);
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//
//        if(!MyActivityLifecycleCallbacks.isAppInForeground()) {
//            Intent forceToTop = new Intent(getApplicationContext(), PersistActivity.class);
//            forceToTop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(forceToTop);
//        }
//        super.onStop();
//    }

    private void startCountDown(long millisUntilFinished, long countDownInterval) {

        // launch persist service
        Intent persistService = new Intent(this, PersistService.class);
        startService(persistService);

        // launch countdown, display time in textview
        final TextView mTimeDisplay = (TextView) findViewById(R.id.time_left_display);

        new CountDownTimer(millisUntilFinished, countDownInterval) {
            // updates text to show time left onTick
            public void onTick(long millisUntilFinished) {
                mTimeDisplay.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            // finished!
            public void onFinish() {
                mTimeDisplay.setText("done!");
                unlockCountDown();
            }
        }.start();
    }

    private void unlockCountDown() {
        // stop PersistService (i.e. unlock user from app)
        Intent stopPersistService = new Intent(getApplicationContext(), PersistService.class);
        stopService(stopPersistService);

        // bring user back to main screen
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        //
    }
}
