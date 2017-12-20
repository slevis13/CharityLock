package comslevis13.github.warlock;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by slevi on 12/15/2017.
 */

public class MakeCallButtonFragment extends android.support.v4.app.Fragment {

    protected Button mDialButton;
    protected OnDialButtonPressedListener mCallback;
    private TelephonyManager mTelephonyManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTelephonyManager = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(
                R.layout.phone_call_button, container, false);
        mDialButton = (Button) fragmentView.findViewById(R.id.call_button);
        mDialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonPress();
//                mCallback.onDialButtonPressed(001);
            }
        });
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // Container Activity must implement this interface
    public interface OnDialButtonPressedListener {
        public void onDialButtonPressed(int flag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDialButtonPressedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnDialButtonPressedListener");
        }
    }

    private void handleButtonPress() {
        mCallback.onDialButtonPressed(001);
    }

}
