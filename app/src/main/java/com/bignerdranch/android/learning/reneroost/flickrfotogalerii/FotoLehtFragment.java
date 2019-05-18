package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class FotoLehtFragment extends NahtavFragment {
    private static final String ARG_URI = "foto_leht_url";

    private Uri mUri;
    private WebView mVeebiVaade;
    private ProgressBar mProgressiRiba;

    public static FotoLehtFragment uusInstants(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);

        FotoLehtFragment fragment = new FotoLehtFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUri = getArguments().getParcelable(ARG_URI);
    }

    @Override
    public View onCreateView(LayoutInflater taispuhuja, ViewGroup konteiner, Bundle savedInstanceState) {
        View vaade = taispuhuja.inflate(R.layout.fragment_foto_leht, konteiner, false);

        mProgressiRiba = (ProgressBar) vaade.findViewById(R.id.progressi_riba);
        mProgressiRiba.setMax(100);

        mVeebiVaade = (WebView) vaade.findViewById(R.id.veebi_vaade);
        mVeebiVaade.getSettings().setJavaScriptEnabled(true);
        mVeebiVaade.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView veebiVaade, int uusProgress) {
                if (uusProgress == 100) {
                    mProgressiRiba.setVisibility(View.GONE);
                } else {
                    mProgressiRiba.setVisibility(View.VISIBLE);
                    mProgressiRiba.setProgress(uusProgress);
                }
            }
            public void onReceivedTitle(WebView veebiVaade, String pealkiri) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(pealkiri);
            }
        });
        mVeebiVaade.setWebViewClient(new WebViewClient());
        mVeebiVaade.loadUrl(mUri.toString());

        return vaade;
    }

}
