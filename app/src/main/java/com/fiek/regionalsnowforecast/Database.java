package com.fiek.regionalsnowforecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLData;

public class Database extends SQLiteOpenHelper {

    public static final String resortsTable = "favoriteResorts";

    public Database(@Nullable Context context) {
        super(context, "BSDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String strQuery = "create table " + resortsTable + " (" +
                FavResortsDBValues.userId + " text primary key, "+
                FavResortsDBValues.resort1 + " integer DEFAULT 0," +
                FavResortsDBValues.resort2 + " integer DEFAULT 0," +
                FavResortsDBValues.resort3 + " integer DEFAULT 0," +
                FavResortsDBValues.resort4 + " integer DEFAULT 0," +
                FavResortsDBValues.resort5 + " integer DEFAULT 0" +
                ")";

        db.execSQL(strQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+resortsTable);
        onCreate(db);
    }

}
