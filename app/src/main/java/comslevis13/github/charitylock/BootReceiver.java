package comslevis13.github.charitylock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by slevi on 12/6/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("got boot broadcast", "got boot broadcast -- ya boy");

            SharedPreferences settings = context.getSharedPreferences(
                    context.getString(R.string.shared_prefs_file_name), 0);
            long millsSaved = settings.getLong(
                    context.getString(R.string.shared_prefs_milliseconds_saved), 0);
            long timeAtShutdown = settings.getLong(
                    context.getString(R.string.shared_prefs_time_at_shutdown), 0);
            long currentTime = System.currentTimeMillis();
            long timeLeftTotal = millsSaved - (currentTime - timeAtShutdown);

            if (timeLeftTotal > 0) {
                Log.d("got shared preferences", "got shared prefs -- ya boy");
                Intent persistActivity = new Intent(context, PersistActivity.class);
                persistActivity.putExtra(
                        context.getString(R.string.dialog_intent_mills), timeLeftTotal);
                context.startActivity(persistActivity);
            }
        }
    }
}
