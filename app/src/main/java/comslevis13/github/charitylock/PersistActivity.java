package comslevis13.github.charitylock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by slevi on 11/27/2017.
 */

public class PersistActivity extends Activity {

    private int timeToLock;

    private int COUNTDOWN_INTERVAL = 100;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persist_activity);

        // get passed-in timeToLock from intent
        Intent intent = getIntent();
        timeToLock = intent.getIntExtra("time_to_lock", 0);

        // generate milliseconds to lock value
        int secondsToLock = timeToLock * 60;
        int millsToLock = secondsToLock * 1000;

        // start countdown and lock user into app
        startCountDown(millsToLock, COUNTDOWN_INTERVAL);
    }

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

    private void unlockCountDown () {
        // stop PersistService (i.e. unlock user from app)
        Intent stopPersistService = new Intent(getApplicationContext(), PersistService.class);
        stopService(stopPersistService);

        // bring user back to main screen
        Intent backToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backToMainActivity);
    }

    @Override
    public void onBackPressed() {
        //
    }



}