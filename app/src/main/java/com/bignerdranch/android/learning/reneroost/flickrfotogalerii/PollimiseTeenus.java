package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollimiseTeenus extends IntentService {

    private static final String SILT = "PollimiseTeenus";

    // Maarab intervalli 1 minut
    private static final long POLLIMISE_INTERVALL_MS = TimeUnit.MINUTES.toMillis(1);

    public static final String TEGEVUS_NAITA_TEADET = "com.bignerdranch.android.learning.reneroost.flickrfotogalerii.NAITA_TEADET";
    public static final String LUBA_PRIVAATNE = "com.bignerdranch.android.learning.reneroost.fotogalerii.PRIVATE";
    public static final String KUTSUNGI_KOOD = "KUTSUNGI_KOOD";
    public static final String TEADE = "TEADE";


    public static Intent uusKavatsus(Context kontekst) {
        return new Intent(kontekst, PollimiseTeenus.class);
    }

    public static void maaraTeenuseAlarm(Context kontekst, boolean onSees) {
        Intent kavatsus = PollimiseTeenus.uusKavatsus(kontekst);
        PendingIntent pKavatsus = PendingIntent.getService(kontekst, 0, kavatsus, 0);
        AlarmManager alarmiHaldur = (AlarmManager) kontekst.getSystemService(Context.ALARM_SERVICE);

        if (onSees) {
            alarmiHaldur.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                    POLLIMISE_INTERVALL_MS, pKavatsus);
        } else {
            alarmiHaldur.cancel(pKavatsus);
            pKavatsus.cancel();
        }
        ParinguEelistused.lulitaAlarmSisse(kontekst, onSees);
    }

    public static boolean onTeenuseAlarmSees(Context kontekst) {
        Intent kavatsus = PollimiseTeenus.uusKavatsus(kontekst);
        PendingIntent pKavatsus = PendingIntent.getService(kontekst, 0,
                kavatsus, PendingIntent.FLAG_NO_CREATE);
        return pKavatsus != null;
    }

    public PollimiseTeenus() {
        super(SILT);
    }

    @Override
    protected void onHandleIntent(Intent kavatsus) {
        if (!onInternetSaadavalJaUhendatud()) {
            return;
        }

        String paring = ParinguEelistused.saaHoiulOlevParing(this);
        String viimaseTulemuseId = ParinguEelistused.saaViimaseTulemuseId(this);
        List<GaleriiUksus> uksused;

        if (paring == null) {
            uksused = new FlickristTombaja().tombaHiljutiFotosid();
        } else {
            uksused = new FlickristTombaja().otsiFotosid(paring);
        }

        if (uksused.size() == 0) {
            return;
        }

        String tulemuseId = uksused.get(0).saaId();
        if (tulemuseId.equals(viimaseTulemuseId)) {
            Log.w(SILT, "Sain vana tulemuse: " + tulemuseId);
        } else {
            Log.w(SILT, "Sain uue tulemuse: " + tulemuseId);

            Resources ressursid = getResources();
            Intent k = FlickrFotoGaleriiAcitivity.uusKavatsus(this);
            PendingIntent pKavatsus = PendingIntent.getActivity(this, 0, k, 0);

            Notification teade = new Notification.Builder(this)
                    .setTicker(ressursid.getString(R.string.uued_pildid_pealkiri))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(ressursid.getString(R.string.uued_pildid_pealkiri))
                    .setContentText(ressursid.getString(R.string.uued_pildid_tekst))
                    .setContentIntent(pKavatsus)
                    .setAutoCancel(true)
                    .build();

            naitaTaustaTeadet(0, teade);
        }

        ParinguEelistused.maaraViimaseTulemuseId(this, tulemuseId);
    }

    private void naitaTaustaTeadet(int kutsungiKood, Notification teade) {
        Intent kavatsus = new Intent(TEGEVUS_NAITA_TEADET);
        kavatsus.putExtra(KUTSUNGI_KOOD, kutsungiKood);
        kavatsus.putExtra(TEADE, teade);
        sendOrderedBroadcast(kavatsus, LUBA_PRIVAATNE, null, null,
        Activity.RESULT_OK, null, null);
    }

    private boolean onInternetSaadavalJaUhendatud() {
        ConnectivityManager uhendusHaldur = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean onInternetSaadaval = uhendusHaldur.getActiveNetworkInfo() != null;
        boolean onInternetUhendatud = onInternetSaadaval && uhendusHaldur.getActiveNetworkInfo().isConnected();

        return onInternetUhendatud;
    }

}
