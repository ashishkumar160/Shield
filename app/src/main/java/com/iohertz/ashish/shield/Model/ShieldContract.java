package com.iohertz.ashish.shield.Model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ashish on 20/2/17.
 */

public final class ShieldContract {

    public static final String CONTENT_AUTHORITY = "com.iohertz.ashish.shield";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SYSTEMS = "systems";
    public static final String PATH_USERS = "users";
    public static final String PATH_DEVICES = "devices";

    private ShieldContract() {}

    public static final class SystemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SYSTEMS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYSTEMS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SYSTEMS;

        public final static String TABLE_NAME = "systems";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SYSTEM_NAME = "name";
        public final static String COLUMN_SYSTEM_ID = "systemid";
    }

    public static final class UserEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEVICES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEVICES;
        public final static String TABLE_NAME = "users";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USER_NAME = "name";
        public final static String COLUMN_USER_EMAIL = "email";
        public final static String COLUMN_USER_ISPRIMARY = "isprimary";
        public final static String COLUMN_USER_SYSTEM_ID = "systemid";
    }

    public static final class DeviceEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DEVICES);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        public final static String TABLE_NAME = "devices";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DEVICE_NAME = "name";
        public final static String COLUMN_DEVICE_ID = "deviceid";
        public final static String COLUMN_DEVICE_SYSTEM_ID = "systemid";
    }
}
