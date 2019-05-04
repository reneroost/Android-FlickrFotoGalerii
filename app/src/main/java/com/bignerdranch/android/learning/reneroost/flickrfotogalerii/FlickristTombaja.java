package com.bignerdranch.android.learning.reneroost.flickrfotogalerii;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickristTombaja {

    private static final String SILT = "FlickristTombaja";
    private static final String API_VOTI = "eb37579ca442b52ba27206ed9d582a59";

    public byte[] saaUrlBaidid(String urlAadress) throws IOException {
        URL url = new URL(urlAadress);
        HttpURLConnection uhendus = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream valja = new ByteArrayOutputStream();
            InputStream sisse = uhendus.getInputStream();

            if (uhendus.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(uhendus.getResponseMessage() + ": koos " + urlAadress);
            }

            int baiteLoetud = 0;
            byte[] puhver = new byte[1024];
            while ((baiteLoetud = sisse.read(puhver)) > 0) {
                valja.write(puhver, 0, baiteLoetud);
            }
            valja.close();
            return valja.toByteArray();
        } finally {
             uhendus.disconnect();
        }
    }

    public String saaUrlString(String urlAadress) throws IOException {
        return new String(saaUrlBaidid(urlAadress));
    }

    public List<GaleriiUksus> tombaUksusi() {

        List<GaleriiUksus> galeriiUksused = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_VOTI)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = saaUrlString(url);
            Log.w(SILT, "Sain JSONi: " + jsonString);
            JSONObject jsonKeha = new JSONObject(jsonString);
            parsiUksusi(galeriiUksused, jsonKeha);
        } catch (IOException ioe) {
            Log.e(SILT, "Ei õnnestunud uksusi tommata. " + ioe);
        } catch (JSONException je) {
            Log.e(SILT, "Ei onnestunud JSONi parserdada. " + je);
        }
        return galeriiUksused;
    }

    private void parsiUksusi(List<GaleriiUksus> galeriiUksused, JSONObject jsonKeha) throws IOException, JSONException {

        JSONObject fotodJsonObjekt = jsonKeha.getJSONObject("photos");
        JSONArray fotoJsonMassiiv = fotodJsonObjekt.getJSONArray("photo");

        for (int i = 0; i < fotoJsonMassiiv.length(); i++) {
            JSONObject fotoJsonObjekt = fotoJsonMassiiv.getJSONObject(i);

            GaleriiUksus galeriiUksus = new GaleriiUksus();
            galeriiUksus.maaraId(fotoJsonObjekt.getString("id"));
            galeriiUksus.maaraPealkiri(fotoJsonObjekt.getString("title"));

            if (!fotoJsonObjekt.has("url_s")) {
                continue;
            }

            galeriiUksus.maaraUrl(fotoJsonObjekt.getString("url_s"));
            galeriiUksused.add(galeriiUksus);
        }
    }
}
