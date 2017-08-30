package com.iohertz.ashish.shield.Model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by ashish on 22/2/17.
 */

public class ShieldProvider extends ContentProvider {

    public static final String LOG_TAG = ShieldProvider.class.getSimpleName();

    private static final int SYSTEMS = 100;
    private static final int SYSTEMS_ID = 101;
    private static final int USERS = 102;
    private static final int USERS_ID = 103;
    private static final int DEVICES = 104;
    private static final int DEVICES_ID = 105;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private ShieldDbHelper mDbHelper;

    static {
        sUriMatcher.addURI(ShieldContract.CONTENT_AUTHORITY, ShieldContract.PATH_SYSTEMS, SYSTEMS);
        sUriMatcher.addURI(ShieldContract.CONTENT_AUTHORITY, ShieldContract.PATH_SYSTEMS + "/*", SYSTEMS_ID);
        sUriMatcher.addURI(ShieldContract.CONTENT_AUTHORITY, ShieldContract.PATH_USERS, USERS);
        sUriMatcher.addURI(ShieldContract.CONTENT_AUTHORITY, ShieldContract.PATH_USERS + "/*", USERS_ID);
        sUriMatcher.addURI(ShieldContract.CONTENT_AUTHORITY, ShieldContract.PATH_DEVICES, DEVICES);
        sUriMatcher.addURI(ShieldContract.CONTENT_AUTHORITY, ShieldContract.PATH_DEVICES + "/*", DEVICES_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ShieldDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SYSTEMS :
                cursor = database.query(ShieldContract.SystemEntry.TABLE_NAME, projection, selection,selectionArgs, null, null, sortOrder);
                break;
            case SYSTEMS_ID :
                selection = ShieldContract.SystemEntry.COLUMN_SYSTEM_ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                cursor = database.query(ShieldContract.SystemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USERS :
                //selection = ShieldContract.UserEntry.COLUMN_USER_SYSTEM_ID + "=?";
                //selectionArgs = new String[] { }
                cursor = database.query(ShieldContract.UserEntry.TABLE_NAME, projection, selection,selectionArgs, null, null, sortOrder);
                break;
            case USERS_ID :
                selection = ShieldContract.UserEntry.COLUMN_USER_EMAIL + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                cursor = database.query(ShieldContract.SystemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DEVICES :
                cursor = database.query(ShieldContract.DeviceEntry.TABLE_NAME, projection, selection,selectionArgs, null, null, sortOrder);
                break;
            case DEVICES_ID :
                selection = ShieldContract.DeviceEntry.COLUMN_DEVICE_ID + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                cursor = database.query(ShieldContract.DeviceEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI "+uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SYSTEMS:
                return ShieldContract.SystemEntry.CONTENT_LIST_TYPE;
            case SYSTEMS_ID:
                return ShieldContract.SystemEntry.CONTENT_ITEM_TYPE;
            case USERS:
                return ShieldContract.UserEntry.CONTENT_LIST_TYPE;
            case USERS_ID:
                return ShieldContract.UserEntry.CONTENT_ITEM_TYPE;
            case DEVICES:
                return ShieldContract.DeviceEntry.CONTENT_LIST_TYPE;
            case DEVICES_ID:
                return ShieldContract.DeviceEntry.CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SYSTEMS:
                return insertSystem(uri, values);
            case USERS:
                return insertUser(uri, values);
            case DEVICES:
                return insertDevice(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+uri);
        }
    }

    private Uri insertSystem(Uri uri, ContentValues values) {
        String systemid = values.getAsString(ShieldContract.SystemEntry.COLUMN_SYSTEM_ID);
        if (systemid == null) {
            throw new IllegalArgumentException("System requires an ID");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ShieldContract.SystemEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertUser(Uri uri, ContentValues values) {
        String email = values.getAsString(ShieldContract.UserEntry.COLUMN_USER_EMAIL);
        if (email == null) {
            throw new IllegalArgumentException("User requires an Email-ID");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ShieldContract.UserEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertDevice(Uri uri, ContentValues values) {
        String deviceid = values.getAsString(ShieldContract.DeviceEntry.COLUMN_DEVICE_ID);
        if (deviceid == null) {
            throw new IllegalArgumentException("Device requires an ID");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ShieldContract.DeviceEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SYSTEMS_ID:
                selection = ShieldContract.SystemEntry.COLUMN_SYSTEM_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                return database.delete(ShieldContract.SystemEntry.TABLE_NAME, selection, selectionArgs);
            case USERS_ID:
                selection = ShieldContract.UserEntry.COLUMN_USER_EMAIL + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                return database.delete(ShieldContract.UserEntry.TABLE_NAME, selection, selectionArgs);
            case DEVICES_ID:
                selection = ShieldContract.DeviceEntry.COLUMN_DEVICE_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                return database.delete(ShieldContract.DeviceEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for "+ uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SYSTEMS_ID:
                selection = ShieldContract.SystemEntry.COLUMN_SYSTEM_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                return updateSystem(uri, values, selection, selectionArgs);
            case USERS_ID:
                selection = ShieldContract.UserEntry.COLUMN_USER_EMAIL + "=?";
                selectionArgs = new String[] {uri.getLastPathSegment()};
                return updateUser(uri, values, selection, selectionArgs);
            case DEVICES_ID:
                selection = ShieldContract.DeviceEntry.COLUMN_DEVICE_ID + "=?";
                selectionArgs = new String[]{ uri.getLastPathSegment() };
                return updateDevice(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateSystem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ShieldContract.SystemEntry.COLUMN_SYSTEM_NAME)) {
            String name = values.getAsString(ShieldContract.SystemEntry.COLUMN_SYSTEM_NAME);
            if (name == null) {
                throw new IllegalArgumentException("System requires an Name");
            }
        }
        if(values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return database.update(ShieldContract.SystemEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private int updateUser(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ShieldContract.UserEntry.COLUMN_USER_NAME)) {
            String name = values.getAsString(ShieldContract.UserEntry.COLUMN_USER_NAME);
            if (name == null) {
                throw new IllegalArgumentException("User requires an Name");
            }
        }
        if(values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return database.update(ShieldContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private int updateDevice(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ShieldContract.DeviceEntry.COLUMN_DEVICE_NAME)) {
            String name = values.getAsString(ShieldContract.DeviceEntry.COLUMN_DEVICE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Device requires an Name");
            }
        }
        if(values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        return database.update(ShieldContract.DeviceEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
