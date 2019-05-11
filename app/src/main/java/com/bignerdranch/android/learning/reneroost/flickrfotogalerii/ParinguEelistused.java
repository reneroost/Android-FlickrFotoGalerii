package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.content.Context;
import android.preference.PreferenceManager;

public class ParinguEelistused {

    private static final String EELISTUS_OTSINGU_PARING = "otsinguParing";

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

}
