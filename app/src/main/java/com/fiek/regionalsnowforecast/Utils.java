package com.fiek.regionalsnowforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static final String PREFS_NAME = "RESORTS_APP";
    public static final String FAVORITES = "Favorite_Resorts";
    public RequestQueue mQueue;

    public Utils() {
        super();
    }

    public void saveFavorites(Context context, List<Resorts> favorites) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.commit();
    }

    public void addFavorite(Context context, Resorts resorts) {
        List<Resorts> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Resorts>();
        favorites.add(resorts);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Resorts resorts) {
        ArrayList<Resorts> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(resorts);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Resorts> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        List<Resorts> favorites;


        if (prefs.contains(FAVORITES)) {
            String jsonFavorites = prefs.getString(FAVORITES, null);
            Gson gson = new Gson();
            Resorts[] favoriteItems = gson.fromJson(jsonFavorites,
                    Resorts[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Resorts>(favorites);
        } else
            return null;

        return (ArrayList<Resorts>) favorites;
    }


    public static String SHA256(EditText etInput) {
        CharSequence strPassword = etInput.getText();
        String pw = strPassword.toString();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pw.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void jsonParse(final TextView tv1, final TextView tv2, final TextView tv3, final ImageView iv1, final Context context, String locationId, String appId, String appKey) {

        String url = "https://api.weatherunlocked.com/api/resortforecast/" + locationId + "?num_of_days=1&app_id=" + appId + "&app_key=" + appKey;
        mQueue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("forecast");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject forecast = jsonArray.getJSONObject(i);

                                if (forecast.getString("time").equals("01:00")) {

                                    double snow_mm = forecast.getDouble("snow_mm");
                                    double hum_pct = forecast.getDouble("hum_pct");
                                    tv1.setText(String.valueOf(snow_mm));
                                    tv2.setText(String.valueOf(hum_pct));

                                    for (int j = 0; j < forecast.length(); j++) {
                                        JSONObject base = forecast.getJSONObject("base");
                                        if (base.getString("wx_icon").equals("Clear.gif")) {
                                            Glide.with(context).load(R.drawable.sunny).into(iv1);
                                        } else {
                                            Toast.makeText(context, "no sunny", Toast.LENGTH_SHORT).show();
                                        }
                                        double temp_c = base.getDouble("temp_c");
                                        tv3.setText(String.valueOf(temp_c));
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }


}
