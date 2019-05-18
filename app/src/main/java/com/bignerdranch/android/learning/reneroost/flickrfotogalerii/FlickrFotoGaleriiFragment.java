package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class FlickrFotoGaleriiFragment extends NahtavFragment {

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
        setHasOptionsMenu(true);
        uuendaUksusi();


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
        // Log.w(SILT, "Taustalõim alustatud.");
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
        // Log.w(SILT, "Taustalõim hävitatud.");
    }

    @Override
    public void onCreateOptionsMenu(Menu menuu, MenuInflater menuuTaispuhuja) {
        super.onCreateOptionsMenu(menuu, menuuTaispuhuja);
        menuuTaispuhuja.inflate(R.menu.fragment_foto_galerii, menuu);

        MenuItem otsinguUksus = menuu.findItem(R.id.menuu_uksus_otsing);
        final SearchView otsinguVaade = (SearchView) otsinguUksus.getActionView();

        otsinguVaade.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String paring) {
                // Log.w(SILT, "QueryTextSubmit: " + paring);
                ParinguEelistused.maaraHoiulOlevParing(getActivity(), paring);
                otsinguVaade.setVisibility(GONE);
                uuendaUksusi();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String paring) {
                // Log.d(SILT, "QueryTextChange: " + paring);
                return false;
            }
        });

        otsinguVaade.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vaade) {
                String paring = ParinguEelistused.saaHoiulOlevParing(getActivity());
                otsinguVaade.setQuery(paring, false);
            }
        });

        MenuItem lulitaUksus = menuu.findItem(R.id.menuu_uksus_lulita_pollimist);
        if (PollimiseTeenus.onTeenuseAlarmSees(getActivity())) {
            lulitaUksus.setTitle(R.string.peata_pollimine);
        } else {
            lulitaUksus.setTitle(R.string.alusta_pollimist);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem uksus) {
        switch (uksus.getItemId()) {
            case R.id.menuu_uksus_puhasta:
                ParinguEelistused.maaraHoiulOlevParing(getActivity(), null);
                uuendaUksusi();
                return true;
            case R.id.menuu_uksus_lulita_pollimist:
                boolean peaksAlarmiKaivitama = !PollimiseTeenus.onTeenuseAlarmSees(getActivity());
                PollimiseTeenus.maaraTeenuseAlarm(getActivity(), peaksAlarmiKaivitama);
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(uksus);
        }
    }


    private void uuendaUksusi() {
        String paring = ParinguEelistused.saaHoiulOlevParing(getActivity());
        new TombaUksusUlesanne(paring).execute();
    }


    private void seadistusAdapter() {
        if (isAdded()) {
            mFotoTaaskasutusVaade.setAdapter(new FotoAdapter(mUksused));
        }
    }

    class FotoHoidja extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mUksusPildiVaade;
        private GaleriiUksus mGaleriiUksus;

        public FotoHoidja(View uksuseVaade) {
            super(uksuseVaade);

            mUksusPildiVaade = (ImageView) uksuseVaade.findViewById(R.id.uksus_pildi_vaade);
            uksuseVaade.setOnClickListener(this);
        }

        public void seoJoonistatav(Drawable joonistatav) {
            mUksusPildiVaade.setImageDrawable(joonistatav);
        }

        public void seoGaleriiUksus(GaleriiUksus galeriiUksus) {
            mGaleriiUksus = galeriiUksus;
        }

        @Override
        public void onClick(View vaade) {
            Intent kavatsus = FotoLehtActivity.uusKavatsus(getActivity(), mGaleriiUksus.saaFotoLeheUri());
            startActivity(kavatsus);
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
            fotoHoidja.seoGaleriiUksus(galeriiUksus);
            Drawable kohahoidja = getResources().getDrawable(R.drawable.allalaadmine);
            fotoHoidja.seoJoonistatav(kohahoidja);
            mPisipildiTombaja.pisipildiJarjekord(fotoHoidja, galeriiUksus.saaUrl());
        }

        @Override
        public int getItemCount() {
            return mGaleriiUksused.size();
        }
    }

    private class TombaUksusUlesanne extends AsyncTask<Void, Void, List<GaleriiUksus>> {

        private String mParing;

        public TombaUksusUlesanne(String paring) {
             this.mParing = paring;
        }

        @Override
        protected List<GaleriiUksus> doInBackground(Void... params) {
            if (mParing == null) {
                return new FlickristTombaja().tombaHiljutiFotosid();
            } else {
                return new FlickristTombaja().otsiFotosid(mParing);
            }
        }

        @Override
        protected  void onPostExecute(List<GaleriiUksus> uksused) {
            mUksused = uksused;
            seadistusAdapter();
        }
    }
}
