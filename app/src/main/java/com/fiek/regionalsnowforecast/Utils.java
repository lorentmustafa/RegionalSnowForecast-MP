package com.fiek.regionalsnowforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.google.gson.Gson;

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

}
