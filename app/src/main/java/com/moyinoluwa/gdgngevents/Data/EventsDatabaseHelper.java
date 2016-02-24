package com.moyinoluwa.gdgngevents.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by moyinoluwa on 2/19/16.
 */
public class EventsDatabaseHelper extends SQLiteOpenHelper {
    private final static String LOG_TAG = EventsDatabaseHelper.class.getSimpleName();

    // Database Name
    private final static String DB_NAME = EventsContract.DATABASE_NAME;

    // Database Version
    private final static int DB_VERSION = 1;

    // Version table
    public final static String EVENTS_TABLE_NAME = EventsContract.Events.TABLE_NAME;
    public final static String EVENTS_ROW_ID = EventsContract.Events.ID;
    public final static String EVENTS_DURATION = EventsContract.Events.DURATION;
    public static final String EVENTS_DATE = EventsContract.Events.EVENT_DATE;
    public static final String EVENTS_NAME = EventsContract.Events.EVENT_NAME;
    public static final String EVENTS_TAGS = EventsContract.Events.EVENT_TAGS;
    public static final String EVENTS_TIME = EventsContract.Events.EVENT_TIME;
    public static final String EVENTS_LONG_DESCRIPTION = EventsContract.Events.LONG_DESCRIPTION;
    public static final String EVENTS_SHORT_DESCRIPTION = EventsContract.Events.SHORT_DESCRIPTION;
    public static final String EVENTS_SPEAKERS = EventsContract.Events.SPEAKERS;
    public static final String EVENTS_VENUE = EventsContract.Events.VENUE;
    public static final String EVENTS_GDG = EventsContract.Events.GDG;

    // SQL statement to create the Version table
    private final static String EVENTS_TABLE_CREATE =
            "CREATE TABLE " +
                    EVENTS_TABLE_NAME + "(" +
                    EVENTS_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    EVENTS_DURATION + " TEXT, " +
                    EVENTS_DATE + " TEXT, " +
                    EVENTS_NAME + " TEXT, " +
                    EVENTS_TAGS + " TEXT, " +
                    EVENTS_TIME + " TEXT, " +
                    EVENTS_LONG_DESCRIPTION + " TEXT, " +
                    EVENTS_SHORT_DESCRIPTION + " TEXT, " +
                    EVENTS_SPEAKERS + " TEXT, " +
                    EVENTS_VENUE + " TEXT, " +
                    EVENTS_GDG + " TEXT" + ");";

    // SQL statement to delete the version table
    private static final String SQL_DELETE_ENTIRES =
            "DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME;

    public EventsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EVENTS_TABLE_CREATE);
        Log.e(LOG_TAG, "Creating table with query:" + EVENTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database. Existing contents will be lost. ["
                + oldVersion + "]->[" + newVersion + "]");

        onCreate(db);
    }
}
