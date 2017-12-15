package comslevis13.github.warlock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by slevi on 12/14/2017.
 */

public class PersistCallActivity extends PersistActivityBase {

    private Button mDialAndCallButton;
    private EditText mPhoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.persist_activity_call_test);

//        mPhoneNumberEditText = findViewById(R.id.phone_number_edit_text);
//        mDialAndCallButton = findViewById(R.id.button_dial_and_call);

//        mDialAndCallButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                handlePhoneCall();
//            }
//        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.persist_activity_call;
    }
}

//    private void handleButtonPress() {
//
//        // todo: launch new activity with altered layout
//        Log.d("button press", "button press -- ya boy");
//        // check that telephony enabled on device
//        if (isTelephonyEnabled(mTelephonyManager)) {
//            // check permission; if not granted, request it
//            // todo: UI to input #
//            handlePhoneCall();
//        }
//        // if telephony disabled, disable call button
//        else {
//            Log.d("telephony", "telephony not enabled -- ya boy");
//            Intent backToPersist = new Intent(this, PersistActivity.class);
//            startActivity(backToPersist);
//            finish();
//        }
//    }
//
//    private boolean isTelephonyEnabled(TelephonyManager telephonyManager) {
//        if (telephonyManager != null) {
//            if (telephonyManager.getSimState() ==
//                    TelephonyManager.SIM_STATE_READY) {
//                // device has telephony enabled
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean isPhonePermissionEnabled() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
//                PackageManager.PERMISSION_GRANTED) {
//            // permission not yet granted
//            Toast.makeText(this, "Phone permission not enabled!",
//                    Toast.LENGTH_LONG).show();
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private void handlePhoneCall() {
//        String phoneNumber = getPhoneNumber();
//        if (isPhoneNumberDialable(phoneNumber)) {
//            stopPersistService();
//            if (PhoneNumberUtils.isEmergencyNumber(phoneNumber)) {
//                // dial emergency number in dialer
//                Uri emergencyNumber = Uri.parse("tel:" + phoneNumber);
//                Intent emergencyIntent = new Intent(Intent.ACTION_DIAL, emergencyNumber);
//                startActivity(emergencyIntent);
//                // stop lock and countdown, update notification
//                stopCountdownAndSendDoneNotification(mCountdownTimer);
//            }
//            else {
//                makePhoneCall(phoneNumber);
//            }
//        }
//        // todo: fix bug. might have to do with persistService stopping, not fully cancelling timer task before call intent?
//        // todo: might have to bind service so can
//
//        else {
//            // not emergency number
//            // listener to restart lock when call ends
//
//            // make call
//            makePhoneCall(phoneNumber);
//        }
//    }
//
//    private String getPhoneNumber() {
//        return mPhoneNumberEditText.getText().toString();
//    }
//
//    private boolean isPhoneNumberDialable(String mPhoneNumber) {
//        //
//        return true;
//    }
//
//    @SuppressLint("MissingPermission")
//    private void makePhoneCall(String mPhoneNumber) {
//        if (!isPhonePermissionEnabled()) {
//            return;
//        }
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + mPhoneNumber));
//        startActivity(callIntent);
//    }
//}
