package com.example.micha.chavrutamatch.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.micha.chavrutamatch.Data.ChavrutaContract.ChavrutaHostEntry;

/**
 * Created by micha on 7/14/2017.
 */


public class ChavrutaMatchDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "chavrutaMatch.db";

    // database version w/o schema change
    private static final int DATABASE_VERSION = 1;

    public ChavrutaMatchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create table for ChavrutaHostEntry class
        final String SQL_CREATE_HOST_ENTRY_TABLE = "CREATE TABLE " + ChavrutaHostEntry.TABLE_NAME + " (" +
                ChavrutaHostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChavrutaHostEntry.HOST_FIRST_NAME + " TEXT NOT NULL, " +
                ChavrutaHostEntry.HOST_LAST_NAME + " INTEGER NOT NULL, " +
                ChavrutaHostEntry.HOST_USER_NAME + " INTEGER NOT NULL, " +

                ChavrutaHostEntry.SESSION_MESSAGE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ChavrutaHostEntry.SESSION_DATE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChavrutaHostEntry.START_TIME + " TEXT NOT NULL, " +
                ChavrutaHostEntry.END_TIME + " INTEGER NOT NULL, " +
                ChavrutaHostEntry.SEFER + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ChavrutaHostEntry.LOCATION + " TEXT NOT NULL, " +
                ChavrutaHostEntry.GUEST_FIRST_NAME+ " INTEGER NOT NULL, " +
                ChavrutaHostEntry.GUEST_LAST_NAME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ChavrutaHostEntry.SESSION_CONFIRMED_BOOL + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChavrutaHostEntry.SESSION_FREQUENCY + " INTEGER NOT NULL, " +
                ChavrutaHostEntry.SESSION_CREATION_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ChavrutaHostEntry.HOST_AVATAR + " INTEGER NOT NULL, " +
                ChavrutaHostEntry.REQUESTING_GUEST_PROFILE + " INTEGER NOT NULL, " +

                ChavrutaHostEntry.SESSION_ID + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_HOST_ENTRY_TABLE);
    }

    //updates on schema change
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChavrutaHostEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}