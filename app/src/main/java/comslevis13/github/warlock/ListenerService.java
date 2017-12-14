package comslevis13.github.warlock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by slevi on 12/12/2017.
 */

public class ListenerService extends Service {

    private TelephonyManager mTelephonyManager;
    private EndCallListener mCallListener;

    @Override
    public void onCreate() {
        super.onCreate();
        startListener();
    }

    private void startListener() {
        mCallListener = new EndCallListener(getApplicationContext());
        mTelephonyManager =
                (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(mCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    public TelephonyManager getmTelephonyManager() {
        return mTelephonyManager;
    }

    protected EndCallListener getmCallListener() {
        return mCallListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mTelephonyManager.listen(mCallListener, PhoneStateListener.LISTEN_NONE);
//        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
