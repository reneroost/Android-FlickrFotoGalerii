package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.content.Context;
import android.preference.PreferenceManager;

public class ParinguEelistused {

    private static final String EELISTUS_OTSINGU_PARING = "otsinguParing";
    private static final String EELISTUS_VIIMASE_TULEMUSE_ID = "viimaseTulemuseId";
    private static final String EELISTUS_ON_ALARM_SEES = "onAlarmSees";

    public static String saaHoiulOlevParing(Context kontekst) {
        return PreferenceManager.getDefaultSharedPreferences(kontekst)
                .getString(EELISTUS_OTSINGU_PARING, null);
    }

    public static void maaraHoiulOlevParing(Context kontekst, String paring) {
        PreferenceManager.getDefaultSharedPreferences(kontekst)
                .edit()
                .putString(EELISTUS_OTSINGU_PARING, paring)
                .apply();
    }

    public static String saaViimaseTulemuseId(Context kontekst) {
        return PreferenceManager.getDefaultSharedPreferences(kontekst)
                .getString(EELISTUS_VIIMASE_TULEMUSE_ID, null);
    }

    public static void maaraViimaseTulemuseId(Context kontekst, String viimaseTulemuseId) {
        PreferenceManager.getDefaultSharedPreferences(kontekst)
                .edit()
                .putString(EELISTUS_VIIMASE_TULEMUSE_ID, viimaseTulemuseId)
                .apply();
    }

    public static boolean onAlarmSees(Context kontekst) {
        return PreferenceManager.getDefaultSharedPreferences(kontekst)
                .getBoolean(EELISTUS_ON_ALARM_SEES, false);
    }

    public static void lulitaAlarmSisse(Context kontekst, boolean onSees) {
        PreferenceManager.getDefaultSharedPreferences(kontekst)
                .edit()
                .putBoolean(EELISTUS_ON_ALARM_SEES, onSees)
                .apply();
    }
}
