package comslevis13.github.warlock;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by slevi on 12/14/2017.
 */

public class PersistBaseFragment extends android.support.v4.app.Fragment {

    protected int COUNTDOWN_INTERVAL = 100;
    protected long MILLISECONDS_IN_HOUR = 3600000;
    protected long MILLISECONDS_IN_MINUTE = 60000;
    protected long MILLISECONDS_IN_SECOND = 1000;

    protected TextView hoursLeftTextView;
    protected TextView minutesLeftTextView;
    protected TextView secondsLeftTextView;
    protected TextView timeLeftTitle;

    protected long hrs;
    protected long mins;
    protected long secs;
    protected long millsLeft;
    protected CountDownTimer mCountdownTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // get passed-in timeToLock from intent
            Intent intent = getActivity().getIntent();
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
        startCountDown(millsLeft, COUNTDOWN_INTERVAL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(
                R.layout.persist_base_fragment, container, false);
        hoursLeftTextView = (TextView) fragmentView.findViewById(R.id.text_view_hours_left_persist);
        minutesLeftTextView = (TextView) fragmentView.findViewById(R.id.text_view_minutes_left_persist);
        secondsLeftTextView = (TextView) fragmentView.findViewById(R.id.text_view_seconds_left_persist);
        timeLeftTitle = (TextView) fragmentView.findViewById(R.id.time_left_title);

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void startCountDown(long millisUntilFinished, long countDownInterval) {
        // launch persist service
        startPersistService();
        // launch countdown, display time
        mCountdownTimer = new CountDownTimer(millisUntilFinished, countDownInterval) {
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

        updateNotification();
    }

    protected void stopCountdownAndSendDoneNotification() {
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }
        millsLeft = 0;
        stopPersistService();
        sendDoneNotification();
    }

    protected void unlockAndFinish() {
        stopCountdownAndSendDoneNotification();
        stopListenerService();
        if (timeLeftTitle != null) {
            timeLeftTitle.setText(getString(R.string.persist_text_on_finish));
        }
        // bring user back to main screen
        Intent mainActivity = new Intent(getActivity(), MainActivity.class);
        startActivity(mainActivity);
        getActivity().finish();
    }

    private void startPersistService() {
        Intent persistService = new Intent(getActivity(), PersistService.class);
        getActivity().startService(persistService);
    }
    protected void stopPersistService() {
        // stop PersistService (i.e. unlock user from app)
        Intent persistService = new Intent(getActivity(), PersistService.class);
        getActivity().stopService(persistService);
    }

    private void stopListenerService() {
        Intent listenerService = new Intent(getActivity(), ListenerService.class);
        getActivity().stopService(listenerService);

    }

    private void updateNotification() {
        String notificationMessageString =
                Long.toString(hrs) + ": " + Long.toString(mins) + ": " + Long.toString(secs);
        // during rotation (and other things), activity may be null
        if (getActivity() == null) {
            return;
        }
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // get notification object
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(notificationMessageString)
                        .setPriority(NotificationCompat.PRIORITY_LOW);
        // fire notification
        if (mNotificationManager != null) {
            mNotificationManager.notify(001, mBuilder.build());
        }
    }

    private void sendDoneNotification() {
        String doneNotificationMessage = "Done!";
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.notification_icon_unlock)
                        .setContentTitle("Unlocked")
                        .setContentText(doneNotificationMessage)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (mNotificationManager != null) {
            mNotificationManager.notify(001, mBuilder.build());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        storeTimesInSharedPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // update sharedPreferences with timeLeft and currentTime, in milliseconds
    private void storeTimesInSharedPreferences() {
        SharedPreferences settings = getActivity().getSharedPreferences(
                getString(R.string.shared_prefs_file_name), 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(getString(R.string.shared_prefs_milliseconds_saved), millsLeft);
        editor.putLong(getString(R.string.shared_prefs_time_at_shutdown),
                System.currentTimeMillis());

        editor.apply();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save mills left and current time in mills
        outState.putLong(getString(R.string.persist_mills_left_save_key), millsLeft);
        outState.putLong(getString(R.string.persist_mills_current_save_key),
                System.currentTimeMillis());

        super.onSaveInstanceState(outState);
    }

}
