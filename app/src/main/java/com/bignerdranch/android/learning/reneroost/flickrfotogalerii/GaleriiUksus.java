package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.net.Uri;

public class GaleriiUksus {

    private String mPealkiri;
    private String mId;
    private String mUrl;
    private String mOmanik;

    @Override
    public String toString() {
        return mPealkiri;
    }

    public void maaraPealkiri(String mPealkiri) {
        this.mPealkiri = mPealkiri;
    }

    public String saaId() {
        return mId;
    }

    public void maaraId(String mId) {
        this.mId = mId;
    }

    public String saaUrl() {
        return mUrl;
    }

    public void maaraUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String saaOmanik() {
        return mOmanik;
    }


    public void maaraOmanik(String omanik) {
        mOmanik = omanik;
    }

    public Uri saaFotoLeheUri() {
        return Uri.parse("https://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(mOmanik)
                .appendPath(mId)
                .build();
    }

}
