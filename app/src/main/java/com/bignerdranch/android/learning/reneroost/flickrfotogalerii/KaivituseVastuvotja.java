package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class KaivituseVastuvotja extends BroadcastReceiver {

    public static final String SILT = "KaivituseVastuvotja";

    @Override
    public void onReceive(Context kontekst, Intent kavatsus) {
        Log.w(SILT, "Sain ülekande kavatsuse: " + kavatsus.getAction());

        boolean onSees = ParinguEelistused.onAlarmSees(kontekst);
        PollimiseTeenus.maaraTeenuseAlarm(kontekst, onSees);
    }
}
