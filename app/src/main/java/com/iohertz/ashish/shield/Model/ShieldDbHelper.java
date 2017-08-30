package com.iohertz.ashish.shield.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ashish on 20/2/17.
 */

public class ShieldDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Shield.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_SYSTEM_ENTRIES =
            "CREATE TABLE " + ShieldContract.SystemEntry.TABLE_NAME + " (" +
                    ShieldContract.SystemEntry._ID + " INTEGER PRIMARY KEY," +
                    ShieldContract.SystemEntry.COLUMN_SYSTEM_ID + " TEXT UNIQUE NOT NULL, " +
                    ShieldContract.SystemEntry.COLUMN_SYSTEM_NAME + " TEXT,"+
                    " UNIQUE (" + ShieldContract.SystemEntry.COLUMN_SYSTEM_ID + ") ON CONFLICT REPLACE);";

    private static final String SQL_CREATE_USER_ENTRIES =
            "CREATE TABLE " + ShieldContract.UserEntry.TABLE_NAME + " (" +
                    ShieldContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    ShieldContract.UserEntry.COLUMN_USER_NAME + " TEXT," +
                    ShieldContract.UserEntry.COLUMN_USER_EMAIL + " TEXT UNIQUE NOT NULL," +
                    ShieldContract.UserEntry.COLUMN_USER_ISPRIMARY + " INTEGER," +
                    ShieldContract.UserEntry.COLUMN_USER_SYSTEM_ID + " TEXT," +
                    " FOREIGN KEY (" + ShieldContract.UserEntry.COLUMN_USER_SYSTEM_ID + ") REFERENCES " +
                    ShieldContract.SystemEntry.TABLE_NAME + " (" + ShieldContract.SystemEntry.COLUMN_SYSTEM_ID + "), "+
                    " UNIQUE (" + ShieldContract.UserEntry.COLUMN_USER_EMAIL + ") ON CONFLICT REPLACE);";

    private static final String SQL_CREATE_DEVICE_ENTRIES =
            "CREATE TABLE " + ShieldContract.DeviceEntry.TABLE_NAME + " (" +
                    ShieldContract.DeviceEntry._ID + " INTEGER PRIMARY KEY," +
                    ShieldContract.DeviceEntry.COLUMN_DEVICE_ID + " TEXT UNIQUE NOT NULL," +
                    ShieldContract.DeviceEntry.COLUMN_DEVICE_NAME + " TEXT," +
                    ShieldContract.DeviceEntry.COLUMN_DEVICE_SYSTEM_ID + " TEXT," +
                    " FOREIGN KEY (" + ShieldContract.DeviceEntry.COLUMN_DEVICE_SYSTEM_ID + ") REFERENCES " +
                    ShieldContract.SystemEntry.TABLE_NAME + " (" + ShieldContract.SystemEntry.COLUMN_SYSTEM_ID + "), "+
                    " UNIQUE (" + ShieldContract.DeviceEntry.COLUMN_DEVICE_ID + ") ON CONFLICT REPLACE);";

    public ShieldDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SYSTEM_ENTRIES);
        db.execSQL(SQL_CREATE_USER_ENTRIES);
        db.execSQL(SQL_CREATE_DEVICE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ShieldContract.SystemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ShieldContract.DeviceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ShieldContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
}
