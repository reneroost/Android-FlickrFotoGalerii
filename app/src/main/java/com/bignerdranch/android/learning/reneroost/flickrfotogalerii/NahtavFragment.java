package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

public abstract class NahtavFragment extends Fragment {

    public static final String SILT = "NahtavFagment";

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PollimiseTeenus.TEGEVUS_NAITA_TEADET);
        getActivity().registerReceiver(mOnNaitaTeadet, filter,
                PollimiseTeenus.LUBA_PRIVAATNE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOnNaitaTeadet);
    }

    private BroadcastReceiver mOnNaitaTeadet = new BroadcastReceiver() {
        @Override
        public void onReceive(Context kontekst, Intent kavatsus) {
            Log.w(SILT, "tühistan teate");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };
}
