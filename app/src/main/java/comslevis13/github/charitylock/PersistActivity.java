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

    private int timeToLock;

    private int COUNTDOWN_INTERVAL = 100;
    private FragmentManager supportFragmentManager;

    private HomeKeyLocker mHomeKeyLocker;

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

    @Override
    protected void onPause() {

        if(!MyActivityLifecycleCallbacks.isAppInForeground()) {
            Intent forceToTop = new Intent(getApplicationContext(), PersistActivity.class);
            forceToTop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(forceToTop);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {

        if(!MyActivityLifecycleCallbacks.isAppInForeground()) {
            Intent forceToTop = new Intent(getApplicationContext(), PersistActivity.class);
            forceToTop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(forceToTop);
        }
        super.onStop();
    }

    private void startCountDown(long millisUntilFinished, long countDownInterval) {

        // TODO: launcher service start

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

        // bring user back to main screen
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        //
    }

    private void launchConfirmDonation () {
        ConfirmDonation confirmDonation = new ConfirmDonation();
        confirmDonation.show(getSupportFragmentManager(), "launchDialog");
    }

    public void onDialogPositiveClick() {
        // User touched the dialog's positive button
        // TODO: make donation
        // TODO: stop persistservice, etc.
        Intent persistService = new Intent(this, PersistService.class);
        stopService(persistService);

        Intent paymentHandler = new Intent(this, PaymentHandler.class);
        startActivity(paymentHandler);

    }


    public void onDialogNegativeClick() {
        // User touched the dialog's negative button

    }



    public static class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

//        * Manages the state of opened vs closed activities, should be 0 or 1.
//         * It will be 2 if this value is checked between activity B onStart() and
//         * activity A onStop().
//         * It could be greater if the top activities are not fullscreen or have
//         * transparent backgrounds.

        private static int visibleActivityCount = 0;

//        * Manages the state of opened vs closed activities, should be 0 or 1
//         * because only one can be in foreground at a time. It will be 2 if this
//         * value is checked between activity B onResume() and activity A onPause().

        private static int foregroundActivityCount = 0;

//        * Returns true if app has foreground
        public static boolean isAppInForeground(){
            return foregroundActivityCount > 0;
        }

//        * Returns true if any activity of app is visible (or device is sleep when
//         * an activity was visible)
        public static boolean isAppVisible(){
            return visibleActivityCount > 0;
        }

        private Activity activity;

        public MyActivityLifecycleCallbacks(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (this.activity == activity) {
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (this.activity == activity) {
                visibleActivityCount ++;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (this.activity == activity) {
                foregroundActivityCount ++;
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (this.activity == activity) {
                foregroundActivityCount --;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            if (this.activity == activity) {
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (this.activity == activity) {
                visibleActivityCount --;
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (this.activity == activity) {
                activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            }
        }
    }
}
