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
    private static final int INTERVAL = 10;

    private static boolean stopTask;

    @Override
    public void onCreate() {
        super.onCreate();

        stopTask = false;
        startPersistRefresh();

    }

    // launches method to continuously refresh task, i.e. lock user into activity
    private void startPersistRefresh () {
        // start polling task
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // to stop the polling
                if (stopTask){
                    this.cancel();
                }

                // if app not in foreground
                if (isBackgroundRunning(getApplicationContext())){
                    // force PersistActivity to top of stack
                    Intent forceToTop = new Intent(getApplicationContext(), PersistActivity.class);
                    forceToTop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(forceToTop);
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, INTERVAL);
    }

    // helper, checks if app in background (i.e. not in foreground)
    private static boolean isBackgroundRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        // if app is in foreground, then it's not in background
                        return false;
                    }
                }
            }
        }
        return true;
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