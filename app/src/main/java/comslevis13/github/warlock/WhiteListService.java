package comslevis13.github.warlock;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telecom.TelecomManager;
import android.util.Log;

import java.util.List;

/**
 * Created by slevi on 12/9/2017.
 */

public class WhiteListService extends Service {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
//        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
//        startActivity(dialIntent);
//        while (true) {
//            if (!isDialerInForeground(getApplicationContext())) {
//                Intent persistService = new Intent(this, PersistService.class);
//                startService(persistService);
//                stopSelf();
//                break;
//            }
//        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private boolean isDialerInForeground(Context context) {
//        TelecomManager telecom_service = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
//        String dialerPackage = telecom_service.getDefaultDialerPackage();
//
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//
//            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    for (String activeProcess : processInfo.pkgList) {
//                        Log.d("activeProcess", activeProcess + " -- ya boy");
//                        if (activeProcess.equals(dialerPackage)) {
//                            // if app is in foreground, then it's not in background
//
//                            return true;
//                        }
//                    }
//                }
//            }
//        Log.d("dialerInForeground", dialerPackage + " -- ya boy");
//        return false;
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
