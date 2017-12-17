package comslevis13.github.warlock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by slevi on 11/27/2017.
 */

public class PersistActivity extends FragmentActivity
        implements MakeCallButtonFragment.OnDialButtonPressedListener,
        PhoneNumberAndButtonFragment.OnCallButtonPressedListener,
        PhoneNumberAndButtonFragment.OnCancelButtonPressedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.persist_activity);

        if (!isDeviceAPhone()) {
            // don't show dial button for tablets
            return;
        }
        if (findViewById(R.id.frameLayoutForCallElements) != null) {
            if (savedInstanceState == null) {
                MakeCallButtonFragment buttonFragment = new MakeCallButtonFragment();
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frameLayoutForCallElements, buttonFragment).commit();
            }
        }
        Log.d("persist onCreate", "onCreate -- ya boy");
    }

    private boolean isDeviceAPhone() {
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            return telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // disable back button
    }

    @Override
    public void onDialButtonPressed(int flag) {
        //
        PhoneNumberAndButtonFragment phoneInputFragment = new PhoneNumberAndButtonFragment();
        android.support.v4.app.FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frameLayoutForCallElements, phoneInputFragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onCallButtonPressed(int flag) {
        if (flag == 100) {
            // emergency number
            PersistBaseFragment mainFragment = (PersistBaseFragment)
                    getSupportFragmentManager().findFragmentById(R.id.persistBaseFragment);
            mainFragment.stopCountdownAndSendDoneNotification();
        }
        else if (flag == 010) {
            // non-emergency number
            buttonFragmentReplace();
        }
    }

    @Override
    public void onCancelButtonPressed() {
        buttonFragmentReplace();
    }

    private void buttonFragmentReplace() {
        MakeCallButtonFragment buttonFragment = new MakeCallButtonFragment();
        android.support.v4.app.FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frameLayoutForCallElements, buttonFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }


//
//    private void updateNotification() {
//        String notificationMessageString =
//                Long.toString(hrs) + ": " + Long.toString(mins) + ": " + Long.toString(secs);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        // get notification object
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.notification_icon)
//                        .setContentTitle(getString(R.string.notification_title))
//                        .setContentText(notificationMessageString)
//                        .setPriority(NotificationCompat.PRIORITY_LOW);
//        // fire notification
//        if (mNotificationManager != null) {
//            mNotificationManager.notify(001, mBuilder.build());
//        }
//    }
//
//    private void sendDoneNotification() {
//        String doneNotificationMessage = "Done!";
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.notification_icon_unlock)
//                        .setContentTitle("Unlocked")
//                        .setContentText(doneNotificationMessage)
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        if (mNotificationManager != null) {
//            mNotificationManager.notify(001, mBuilder.build());
//        }
//    }

}
