package comslevis13.github.warlock;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by slevi on 12/9/2017.
 */

public class EndCallListener extends PhoneStateListener {

    private boolean hasCallBeenMade = false;
    private Context mContext;

    // retrieve context to make intent calls
    EndCallListener(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if(TelephonyManager.CALL_STATE_RINGING == state) {
            // use this if you want to disable lock for incoming calls as well
            // todo ?
            Log.i("call state", "ya boy RINGING, number: " + incomingNumber);
        }
        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
            //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
            Log.i("call state", "ya boy OFFHOOK");
            hasCallBeenMade = true;
        }
        if(TelephonyManager.CALL_STATE_IDLE == state) {
            //when this state occurs, and your flag is set, restart your app
            Log.i("call state", "ya boy IDLE");
            // call has ended
            if (hasCallBeenMade) {
                // start lock service
                Intent persistService = new Intent(mContext, PersistService.class);
                persistService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startService(persistService);
            }
            hasCallBeenMade = false;
        }
    }

}