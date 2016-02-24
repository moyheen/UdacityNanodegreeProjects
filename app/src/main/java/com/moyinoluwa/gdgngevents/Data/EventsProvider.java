package com.moyinoluwa.gdgngevents.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by moyinoluwa on 2/19/16.
 */
public class EventsProvider extends ContentProvider {
    private EventsDatabaseHelper eventsDatabaseHelper;

    public static final String AUTHORITY = "com.moyinoluwa.gdgngevents.Data.EventsProvider";
    private static final String EVENTS_BASE_PATH = "events";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + EVENTS_BASE_PATH);

    private static final int EVENTS = 100;
    private static final int EVENTS_ID = 110;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, EVENTS_BASE_PATH, EVENTS);
        uriMatcher.addURI(AUTHORITY, EVENTS_BASE_PATH + "/#", EVENTS_ID);
    }

    private String tableName;
    private String basePath;
    protected SQLiteDatabase database;

    public EventsProvider() {
        this.tableName = EventsDatabaseHelper.EVENTS_TABLE_NAME;
        this.basePath = EVENTS_BASE_PATH;
    }


    @Override
    public boolean onCreate() {
        eventsDatabaseHelper = new EventsDatabaseHelper(getContext());
        database = eventsDatabaseHelper.getWritableDatabase();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int uriType = uriMatcher.match(uri);

        switch (uriType) {
            case EVENTS_ID:
                selection = EVENTS_ID + "=" + uri.getLastPathSegment();
                break;
        }

        return database.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(tableName, null, values);
        Uri newUri = Uri.parse(basePath + "/" + id);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(tableName, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(tableName, values, selection, selectionArgs);
    }
}
