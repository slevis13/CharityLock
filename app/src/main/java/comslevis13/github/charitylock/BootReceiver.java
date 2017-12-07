package comslevis13.github.charitylock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by slevi on 12/6/2017.
 */

// persist lock even if user reboots the device
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // receive broadcast when device boots up
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // check if there is time left on lock
            SharedPreferences settings = context.getSharedPreferences(
                    context.getString(R.string.shared_prefs_file_name), 0);
            long millsSaved = settings.getLong(
                    context.getString(R.string.shared_prefs_milliseconds_saved), 0);
            long timeAtShutdown = settings.getLong(
                    context.getString(R.string.shared_prefs_time_at_shutdown), 0);
            long currentTime = System.currentTimeMillis();
            long timeLeftTotal = millsSaved - (currentTime - timeAtShutdown);

            if (timeLeftTotal > 0) {
                // launch persistActivity with new timeLeft value
                Intent persistActivity = new Intent(context, PersistActivity.class);
                persistActivity.putExtra(
                        context.getString(R.string.dialog_intent_mills), timeLeftTotal);
                context.startActivity(persistActivity);
            }
        }
    }
}
