package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FlickrFotoGaleriiFragment extends Fragment {

    private static final String SILT = "FlickrFotoGaleriiFrag";

    private RecyclerView mFotoTaaskasutusVaade;
    private List<GaleriiUksus> mUksused = new ArrayList<>();
    private PisipildiTombaja<FotoHoidja> mPisipildiTombaja;

    public static FlickrFotoGaleriiFragment uusInstants() {
        return new FlickrFotoGaleriiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new TombaUksusUlesanne().execute();

        Handler vastuseKasitleja = new Handler();
        mPisipildiTombaja = new PisipildiTombaja<>(vastuseKasitleja);
        mPisipildiTombaja.maaraPisipildiTombajaKuular(
                new PisipildiTombaja.PisipildiTombajaKuular<FotoHoidja>() {
                    @Override
                    public void onPisipiltTommatud(FotoHoidja fotoHoidja, Bitmap raster) {
                        Drawable joonistatav = new BitmapDrawable(getResources(), raster);
                        fotoHoidja.seoJoonistatav(joonistatav);
                    }
                }
        );

        mPisipildiTombaja.start();
        mPisipildiTombaja.getLooper();
        Log.w(SILT, "Taustalõim alustatud.");
    }

    @Override
    public View onCreateView(LayoutInflater taispuhuja, ViewGroup konteiner, Bundle savedInstanceState) {

        View vaade = taispuhuja.inflate(R.layout.fragment_flickr_foto_galerii, konteiner, false);

        mFotoTaaskasutusVaade = (RecyclerView) vaade.findViewById(R.id.foto_taaskasutus_vaade);
        mFotoTaaskasutusVaade.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        seadistusAdapter();

        return vaade;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPisipildiTombaja.puhastaJarjekord();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPisipildiTombaja.quit();
        Log.w(SILT, "Taustalõim hävitatud.");
    }

    private void seadistusAdapter() {
        if (isAdded()) {
            mFotoTaaskasutusVaade.setAdapter(new FotoAdapter(mUksused));
        }
    }

    class FotoHoidja extends RecyclerView.ViewHolder {
        private ImageView mUksusPildiVaade;

        public FotoHoidja(View uksuseVaade) {
            super(uksuseVaade);

            mUksusPildiVaade = (ImageView) uksuseVaade.findViewById(R.id.uksus_pildi_vaade);
        }

        public void seoJoonistatav(Drawable joonistatav) {
            mUksusPildiVaade.setImageDrawable(joonistatav);
        }
    }

    private class FotoAdapter extends RecyclerView.Adapter<FotoHoidja> {
        private List<GaleriiUksus> mGaleriiUksused;

        public FotoAdapter(List<GaleriiUksus> galeriiUksused) {
            mGaleriiUksused = galeriiUksused;
        }

        @Override
        public FotoHoidja onCreateViewHolder(ViewGroup vaateGrupp, int vaateTuup) {
            LayoutInflater taispuhuja = LayoutInflater.from(getActivity());
            View vaade = taispuhuja.inflate(R.layout.nimekirja_uksus_galerii, vaateGrupp, false);
            return new FotoHoidja(vaade);
        }

        @Override
        public void onBindViewHolder(FotoHoidja fotoHoidja, int positioon) {
            GaleriiUksus galeriiUksus = mGaleriiUksused.get(positioon);
            Drawable kohahoidja = getResources().getDrawable(R.drawable.bill_up_close);
            fotoHoidja.seoJoonistatav(kohahoidja);
            mPisipildiTombaja.pisipildiJarjekord(fotoHoidja, galeriiUksus.saaUrl());
        }

        @Override
        public int getItemCount() {
            return mGaleriiUksused.size();
        }
    }

    private class TombaUksusUlesanne extends AsyncTask<Void, Void, List<GaleriiUksus>> {
        @Override
        protected List<GaleriiUksus> doInBackground(Void... params) {
            return new FlickristTombaja().tombaUksusi();
        }

        @Override
        protected  void onPostExecute(List<GaleriiUksus> uksused) {
            mUksused = uksused;
            seadistusAdapter();
        }
    }
}
