package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlickrFotoGaleriiFragment extends Fragment {

    private static final String SILT = "FlickrFotoGaleriiFrag";

    private RecyclerView mFotoTaaskasutusVaade;
    private List<GaleriiUksus> mUksused = new ArrayList<>();

    public static FlickrFotoGaleriiFragment uusInstants() {
        return new FlickrFotoGaleriiFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new TombaUksusUlesanne().execute();
    }

    @Override
    public View onCreateView(LayoutInflater taispuhuja, ViewGroup konteiner, Bundle savedInstanceState) {

        View vaade = taispuhuja.inflate(R.layout.fragment_flickr_foto_galerii, konteiner, false);

        mFotoTaaskasutusVaade = (RecyclerView) vaade.findViewById(R.id.foto_taaskasutus_vaade);
        mFotoTaaskasutusVaade.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        seadistusAdapter();

        return vaade;
    }

    private void seadistusAdapter() {
        if (isAdded()) {
            mFotoTaaskasutusVaade.setAdapter(new FotoAdapter(mUksused));
        }
    }

    private class FotoHoidja extends RecyclerView.ViewHolder {
        private TextView mPealkirjaTekstiVaade;

        public FotoHoidja(View uksuseVaade) {
            super(uksuseVaade);

            mPealkirjaTekstiVaade = (TextView) uksuseVaade;
        }

        public void seoGaleriiUksus(GaleriiUksus uksus) {
            mPealkirjaTekstiVaade.setText(uksus.toString());
        }
    }

    private class FotoAdapter extends RecyclerView.Adapter<FotoHoidja> {
        private List<GaleriiUksus> mGaleriiUksused;

        public FotoAdapter(List<GaleriiUksus> galeriiUksused) {
            mGaleriiUksused = galeriiUksused;
        }

        @Override
        public FotoHoidja onCreateViewHolder(ViewGroup vaateGrupp, int vaateTuup) {
            TextView tekstiVaade = new TextView(getActivity());
            return new FotoHoidja(tekstiVaade);
        }

        @Override
        public void onBindViewHolder(FotoHoidja fotoHoidja, int positioon) {
            GaleriiUksus galeriiUksus = mGaleriiUksused.get(positioon);
            fotoHoidja.seoGaleriiUksus(galeriiUksus);
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
