package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class FlickrFotoGaleriiAcitivity extends UheFragmendiActivity {

    public static Intent uusKavatsus(Context kontekst) {
        return new Intent(kontekst, FlickrFotoGaleriiAcitivity.class);
    }

    @Override
    protected Fragment looFragment() {
        return FlickrFotoGaleriiFragment.uusInstants();
    }


}
