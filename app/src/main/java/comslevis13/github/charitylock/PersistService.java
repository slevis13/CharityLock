package comslevis13.github.charitylock;

import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PersistService extends Service {

    // poll interval (milliseconds)
    private static final int INTERVAL = 100;

    private static boolean stopTask;

    @Override
    public void onCreate() {
        super.onCreate();

        stopTask = false;
        startPersistRefresh();
    }

    // launches method to continuously refresh activity
    private void startPersistRefresh () {
        // intent to launch PersistActivity
        final Intent forceToTop = new Intent(getApplicationContext(), PersistActivity.class);
        forceToTop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // start polling task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // to stop the polling
                if (stopTask){
                    this.cancel();
                }
                else {
                    // force PersistActivity to foreground
                    startActivity(forceToTop);
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, INTERVAL);
    }

    @Override
    public void onDestroy(){
        stopTask = true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}