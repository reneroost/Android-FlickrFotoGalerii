package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PisipildiTombaja<T> extends HandlerThread {

    private static final String SILT = "PisipildiTombaja";
    private static final int SONUMI_ALLALAADIMINE = 0;

    private boolean mOnLopetanud =  false;
    private Handler mTaotluseKasitleja;
    private ConcurrentMap<T, String> mTaotluseMap = new ConcurrentHashMap<>();
    private Handler mVastuseKasitleja;
    private PisipildiTombajaKuular<FlickrFotoGaleriiFragment.FotoHoidja> mPisipildiTombajaKuular;

    public interface PisipildiTombajaKuular<T> {
        void onPisipiltTommatud(T siht, Bitmap pisipilt);
    }

    public void maaraPisipildiTombajaKuular(PisipildiTombajaKuular<FlickrFotoGaleriiFragment.FotoHoidja> kuular) {
        mPisipildiTombajaKuular = kuular;
    }

    public PisipildiTombaja(Handler vastuseKasitleja) {
        super(SILT);
        mVastuseKasitleja = vastuseKasitleja;
    }

    @Override
    protected void onLooperPrepared() {
        mTaotluseKasitleja = new Handler() {
            @Override
            public void handleMessage(Message sonum) {
                if (sonum.what == SONUMI_ALLALAADIMINE) {
                    T siht = (T) sonum.obj;
                    // Log.w(SILT, "Sain URLi taotluse: " + mTaotluseMap.get(siht));
                    kasitleTaotlust(siht);
                }
            }
        };
    }

    @Override
    public boolean quit() {
        mOnLopetanud = true;
        return super.quit();
    }

    public void pisipildiJarjekord(T siht, String url) {
        // Log.w(SILT, "Sain URLi: " + url);

        if (url == null) {
            mTaotluseMap.remove(siht);
        } else {
            mTaotluseMap.put(siht, url);
            mTaotluseKasitleja.obtainMessage(SONUMI_ALLALAADIMINE, siht).sendToTarget();
        }
    }

    public void puhastaJarjekord() {
        mTaotluseKasitleja.removeMessages(SONUMI_ALLALAADIMINE);
        mTaotluseMap.clear();
    }

    private void kasitleTaotlust(final T siht) {
        try {
            final String url = mTaotluseMap.get(siht);

            if (url == null) {
                return;
            }

            byte[] rasteriBaidid = new FlickristTombaja().saaUrlBaidid(url);
            final Bitmap raster = BitmapFactory.decodeByteArray(rasteriBaidid, 0, rasteriBaidid.length);
            // Log.w(SILT, "Raster loodud");

            mVastuseKasitleja.post(new Runnable() {
                @Override
                public void run() {
                    if (mTaotluseMap.get(siht) != url || mOnLopetanud) {
                        return;
                    }
                    mTaotluseMap.remove(siht);
                    mPisipildiTombajaKuular.onPisipiltTommatud((FlickrFotoGaleriiFragment.FotoHoidja) siht, raster);
                }
            });

        } catch (IOException ioe) {
            // Log.w(SILT, "Viga pildi allalaadimisel", ioe);
        }
    }
}
