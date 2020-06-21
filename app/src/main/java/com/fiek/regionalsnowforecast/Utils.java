package com.fiek.regionalsnowforecast;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    public void addFavResort(Context context, int position, AdapterView<?> view, String userKey) {

        Resorts favResorts = (Resorts) view.getItemAtPosition(position);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = new Database(context).getWritableDatabase();
        cv.put(FavResortsDBValues.userId, String.valueOf(userKey));
        db.insert(Database.resortsTable, null, cv);
        if (favResorts.getrId() == 1) {
            cv.put(FavResortsDBValues.resort1, 1);
        }
        if (favResorts.getrId() == 2) {
            cv.put(FavResortsDBValues.resort2, 1);
        }
        if (favResorts.getrId() == 3) {
            cv.put(FavResortsDBValues.resort3, 1);
        }
        if (favResorts.getrId() == 4) {
            cv.put(FavResortsDBValues.resort4, 1);
        }
        if (favResorts.getrId() == 5) {
            cv.put(FavResortsDBValues.resort5, 1);
        }

        try {
            long retValue = db.update(Database.resortsTable, cv, null, null);
            if (retValue > 0) {
                Toast.makeText(context, context.getResources().getString(R.string.add_favr), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void removeFavResort(Context context, int position, AdapterView<?> view, String userKey) {

        Resorts favResorts = (Resorts) view.getItemAtPosition(position);
        ContentValues cv = new ContentValues();
        SQLiteDatabase readDbObj = new Database(context).getReadableDatabase();
        Cursor c = readDbObj.rawQuery("select * from "+Database.resortsTable+" where "+FavResortsDBValues.userId+"=?", new String[]{userKey});
        if(c.getCount() == 1) {
            c.moveToFirst();
            if (favResorts.getrId() == 1) {
                cv.put(FavResortsDBValues.resort1, 0);
            }
            if (favResorts.getrId() == 2) {
                cv.put(FavResortsDBValues.resort2, 0);
            }
            if (favResorts.getrId() == 3) {
                cv.put(FavResortsDBValues.resort3, 0);
            }
            if (favResorts.getrId() == 4) {
                cv.put(FavResortsDBValues.resort4, 0);
            }
            if (favResorts.getrId() == 5) {
                cv.put(FavResortsDBValues.resort5, 0);
            }
            SQLiteDatabase db = new Database(context).getWritableDatabase();

            try {
                long retValue = db.update(Database.resortsTable, cv, null, null);
                if (retValue > 0) {
                    Toast.makeText(context, context.getResources().getString(R.string.remove_favr), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                db.close();
            }
        }
    }

}
