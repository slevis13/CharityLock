package comslevis13.github.warlock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by slevi on 12/15/2017.
 */


// todo: make back button to get out of input fragment

public class PhoneNumberAndButtonFragment extends Fragment {

    private Button mCallButton;
    private Button mCancelButton;
    private OnCallButtonPressedListener mCallCallback;
    private OnCancelButtonPressedListener mCancelCallback;
    private EditText mPhoneNumberInput;
    private TelephonyManager mTelephonyManager;
    private String mPhoneNumberTest = "911";

    public interface OnCallButtonPressedListener {
        public void onCallButtonPressed(int flag);
    }
    public interface OnCancelButtonPressedListener {
        void onCancelButtonPressed();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallCallback =
                    (PhoneNumberAndButtonFragment.OnCallButtonPressedListener) getActivity();
            mCancelCallback =
                    (PhoneNumberAndButtonFragment.OnCancelButtonPressedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnButtonPressedListeners");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTelephonyManager =
                (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(
                R.layout.phone_input_and_button, container, false);
        // set button listeners
        mCallButton = (Button) fragmentView.findViewById(R.id.button_dial_and_call);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCallButtonPress();
            }
        });
        mCancelButton = (Button) fragmentView.findViewById(R.id.button_cancel_call);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCancelCallback.onCancelButtonPressed();
            }
        });
        mPhoneNumberInput = (EditText) fragmentView.findViewById(R.id.phone_number_edit_text);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void handleCallButtonPress() {
        String phoneNumber = mPhoneNumberInput.getText().toString();
        // emergency number
        if (PhoneNumberUtils.isEmergencyNumber(phoneNumber)) {
            // callback flag for emergency number
            mCallCallback.onCallButtonPressed(100);

            getActivity().finish();
            doEmergencyCall(phoneNumber);
        }
        else {
            // non-emergency number
            if(!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
                Toast.makeText(getActivity(),
                        "Input valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isTelephonyEnabled(mTelephonyManager)) {
                Toast.makeText(getActivity(),
                        "This device cannot call non-emergency numbers",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(!isPhonePermissionEnabled()) {
                Toast.makeText(getActivity(),
                        "Phone permission not enabled", Toast.LENGTH_SHORT).show();
                return;
            }
            unregisterListeners();
            mCallCallback.onCallButtonPressed(010);
            stopPersistService();
            // start call listener
            Intent listenerIntent = new Intent(getActivity(), ListenerService.class);
            getActivity().startService(listenerIntent);
            // make call
            Intent makeCall = new Intent(Intent.ACTION_CALL);
            makeCall.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(makeCall);
        }
    }

    private void doEmergencyCall(String number) {
        // dial number
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }

    public void unregisterListeners() {
        mCancelButton.setOnClickListener(null);
        mCallButton.setOnClickListener(null);
    }

    private void stopPersistService() {
        PersistService.stopPersistTask();
        // stop PersistService (i.e. unlock user from app)
        Intent persistService = new Intent(getActivity(), PersistService.class);
        getActivity().stopService(persistService);
    }

    private boolean isPhonePermissionEnabled() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isTelephonyEnabled(TelephonyManager telephonyManager) {
        return telephonyManager != null &&
                telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }



}
