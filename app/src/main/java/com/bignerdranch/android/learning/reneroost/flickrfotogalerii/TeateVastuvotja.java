package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class TeateVastuvotja extends BroadcastReceiver {

    public static final String SILT = "TeateVastuvotja";

    @Override
    public void onReceive(Context kontekst, Intent kavatsus) {
        Log.w(SILT, "sain vastuse: " + getResultCode());
        if (getResultCode() != Activity.RESULT_OK) {
            return;
        }
        int kutsungiKood = kavatsus.getIntExtra(PollimiseTeenus.KUTSUNGI_KOOD, 0);
        Notification teade = (Notification) kavatsus.getParcelableExtra(PollimiseTeenus.TEADE);

        NotificationManagerCompat teateHaldur = NotificationManagerCompat.from(kontekst);
        teateHaldur.notify(kutsungiKood, teade);
    }
}
