package comslevis13.github.charitylock;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {

    // TODO: time selector UI
    // TODO: what happens when device rotates?
    // TODO: what happens when user gets a phone call, text, other notification?
    // TODO; if user doesn't have wifi/data? could generate json object, send when wifi detected

    private int timeToLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(permissionIntent, 5469);

    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==5469)
//        {
//            setContentView(R.layout.activity_main);
//        }
//    }


    // gets time from time picker, on 'Done' button click
    // called onClick in button in activity.main.xml
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getTime (View view) {
        EditText timePicker = (EditText) findViewById(R.id.time_picker);
        // get input from editText, store in timeToLock
        timeToLock = Integer.parseInt(timePicker.getText().toString());
        // launch confirmation dialog
        launchDialogConfirm();
    }

    private void launchDialogConfirm () {
        ConfirmLockOut confirmLockOut = new ConfirmLockOut();
        confirmLockOut.show(getSupportFragmentManager(), "launchDialog");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface

    public void onDialogPositiveClick() {
        // User touched the dialog's positive button
        // TODO: pass timeToLock to persistactivity
        Intent intent = new Intent(this, PersistActivity.class);
        intent.putExtra("time_to_lock", timeToLock);
        startActivity(intent);

    }


    public void onDialogNegativeClick() {
        // User touched the dialog's negative button

    }
}
