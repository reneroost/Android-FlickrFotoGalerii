package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

public class FotoLehtActivity extends UheFragmendiActivity {

    public static Intent uusKavatsus(Context kontekst, Uri fotoLeheUri) {
        Intent kavatsus = new Intent(kontekst, FotoLehtActivity.class);
        kavatsus.setData(fotoLeheUri);
        return kavatsus;
    }

    @Override
    protected Fragment looFragment() {
        return FotoLehtFragment.uusInstants(getIntent().getData());
    }
}
