package comslevis13.github.charitylock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

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

    private long hrs;
    private long mins;
    private long secs;

    private long millsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.persist_activity);

        if (savedInstanceState == null) {
            // get passed-in timeToLock from intent
            Intent intent = getIntent();
            millsLeft = intent.getLongExtra(getString(R.string.dialog_intent_mills), 0);
        }
        else {
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

        // start countdown and lock user into app
        startCountDown(millsLeft, COUNTDOWN_INTERVAL);
    }

    private void startCountDown(long millisUntilFinished, long countDownInterval) {

        // launch persist service
        Intent persistService = new Intent(this, PersistService.class);
        startService(persistService);

        // launch countdown, display time
        new CountDownTimer(millisUntilFinished, countDownInterval) {
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
    }

    private void unlockAndFinish() {
        timeLeftTitle.setText(getString(R.string.persist_text_on_finish));
        millsLeft = 0;

        // stop PersistService (i.e. unlock user from app)
        Intent persistService = new Intent(getApplicationContext(), PersistService.class);
        stopService(persistService);

        // bring user back to main screen
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
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
//        Intent persistService = new Intent(getApplicationContext(), PersistService.class);
//        stopService(persistService);

        super.onDestroy();
    }
}
