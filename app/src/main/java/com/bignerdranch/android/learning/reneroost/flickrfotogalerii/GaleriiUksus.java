package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

public class GaleriiUksus {

    private String mPealkiri;
    private String mId;
    private String mUrl;

    @Override
    public String toString() {
        return mPealkiri;
    }

    public String saaPealkiri() {
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
}
