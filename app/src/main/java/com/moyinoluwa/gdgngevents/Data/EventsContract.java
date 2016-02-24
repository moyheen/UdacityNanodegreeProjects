package com.moyinoluwa.gdgngevents.Data;

import android.provider.BaseColumns;

/**
 * Created by moyinoluwa on 2/18/16.
 */
public class EventsContract {
    public static final String DATABASE_NAME = "EventsData.db";

    public EventsContract() {
    }

    public static final class Events implements BaseColumns {

        // Define the table name
        public static final String TABLE_NAME = "events";

        // Define table columns
        public static final String ID = BaseColumns._ID;
        public static final String DURATION = "duration";
        public static final String EVENT_DATE = "event_date";
        public static final String EVENT_NAME = "event_name";
        public static final String EVENT_TAGS = "event_tags";
        public static final String EVENT_TIME = "event_time";
        public static final String LONG_DESCRIPTION = "long_description";
        public static final String SHORT_DESCRIPTION = "short_description";
        public static final String SPEAKERS = "speakers";
        public static final String VENUE = "venue";
        public static final String GDG = "gdg";

        // Define projection for event table
        public static final String[] PROJECTION = new String[]{
                EventsContract.Events.ID,
                Events.DURATION,
                Events.EVENT_DATE,
                Events.EVENT_NAME,
                Events.EVENT_TAGS,
                Events.EVENT_TIME,
                Events.LONG_DESCRIPTION,
                Events.SHORT_DESCRIPTION,
                Events.SPEAKERS,
                Events.VENUE,
                Events.GDG
        };
    }
}
