package comslevis13.github.charitylock;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

//Copy this class
public class HomeKeyLocker {
    private OverlayDialog mOverlayDialog;
    public void lock(Activity activity) {
        if (mOverlayDialog == null) {
            mOverlayDialog = new OverlayDialog(activity);
            mOverlayDialog.show();
        }
    }
    public void unlock() {
        if (mOverlayDialog != null) {
            mOverlayDialog.dismiss();
            mOverlayDialog = null;
        }
    }
    private static class OverlayDialog extends AlertDialog {

        public OverlayDialog(Activity activity) {
            super(activity, R.style.OverlayDialog);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (Settings.canDrawOverlays(getContext()) &&
//                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//                }

                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }
                params.dimAmount = 0.0F; // transparent
                params.width = 0;
                params.height = 0;
                params.gravity = Gravity.BOTTOM;
                getWindow().setAttributes(params);
                getWindow().setFlags( WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |  WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, 0xffffff);
                setOwnerActivity(activity);
                setCancelable(false);
            }


//        public final boolean dispatchTouchEvent(MotionEvent motionevent) {
//            return true;
//        }

        protected final void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            FrameLayout framelayout = new FrameLayout(getContext());
            framelayout.setBackgroundColor(0);
            setContentView(framelayout);
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            return true;
        }
    }
}