package com.fiek.regionalsnowforecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public static final String tblUsers = "users";

    public Database(@Nullable Context context, @Nullable String name) {
        super(context, "RSFDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strQuery = "create table "+tblUsers+" (" +
                User.ID + " integer primary key autoincrement,"+
                User.Name +" text not null,"+
                User.Email +" text not null,"+
                User.Address +" text not null,"+
                User.Region +" text not null,"+
                User.Password +" text not null"+
                ")";

        db.execSQL(strQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
