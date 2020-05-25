package com.fiek.regionalsnowforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.preference.PreferenceManager;

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



    public static String SHA256(EditText etInput) {
        CharSequence strPassword = etInput.getText();
        String pw = strPassword.toString();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pw.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while(hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean setBoolPreference(String key, Boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }

    public static Boolean getBoolPreference(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }



    public void saveSession(String id, String email, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("sessionUserId", id);
        editor.putString("sessionUserEmail", email);
        editor.commit();

    }

    public String getSessionId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("sessionUserId", "remove");
    }

    public String getSessionEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        return preferences.getString("sessionUserEmail", "");
    }

    public void removeSession(Context context, String value){
        SharedPreferences preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("sessionUserId", value).commit();
    }


}
