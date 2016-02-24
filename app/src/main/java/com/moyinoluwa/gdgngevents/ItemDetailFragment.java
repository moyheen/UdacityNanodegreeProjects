package com.moyinoluwa.gdgngevents;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.core.SyncTree;
import com.moyinoluwa.gdgngevents.Data.EventsDatabaseHelper;
import com.uber.sdk.android.rides.RequestButton;

import java.util.Vector;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the event items that this fragment
     * represents.
     */

    private String eventName;
    private String duration;
    private String eventDate;
    private String eventTags;
    private String eventTime;
    private String longDescription;
    private String shortDescription;
    private String speakers;
    private String venue;
    private String gdgName;
    private ImageView favButton;
    private LinearLayout shareButton;
    private EventsDatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    public static final String EVENT_NAME = "event_name";
    public static final String SHORT_DESCRIPTION = "short_description";
    public static final String LONG_DESCRIPTION = "long_description";
    public static final String VENUE = "venue";
    public static final String EVENT_DATE = "event_date";
    public static final String EVENT_TIME = "event_time";
    public static final String DURATION = "duration";
    public static final String SPEAKERS = "speakers";
    public static final String EVENT_TAGS = "event_tags";
    public static final String EVENT_GDG = "event_gdg";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EVENT_NAME)) {
            // Load the content specified by the fragment
            // arguments.
            eventName = getArguments().getString(EVENT_NAME);
            duration = getArguments().getString(DURATION);
            eventDate = getArguments().getString(EVENT_DATE);
            eventTags = getArguments().getString(EVENT_TAGS);
            eventTime = getArguments().getString(EVENT_TIME);
            longDescription = getArguments().getString(LONG_DESCRIPTION);
            shortDescription = getArguments().getString(SHORT_DESCRIPTION);
            speakers = getArguments().getString(SPEAKERS);
            venue = getArguments().getString(VENUE);

            sharedPreferences = getActivity().getSharedPreferences("gdg_events", Context.MODE_PRIVATE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                // Retrieve the group associated with this event
                if (getArguments().getString(EVENT_GDG) != null) {
                    gdgName = getArguments().getString(EVENT_GDG);
                } else {
                    gdgName = sharedPreferences.getString("gdg", "GDG");
                }

                // Update the Appbar layout title
                appBarLayout.setTitle(gdgName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        favButton = (ImageView) rootView.findViewById(R.id.favorite_button);
        shareButton = (LinearLayout) rootView.findViewById(R.id.share_button);
        databaseHelper = new EventsDatabaseHelper(getContext());

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create share intent message
                String text = "Join me for " + eventName + " at " + venue + " on " + eventDate + " by " +
                        DateParser.formattedTime(eventTime) + "\n" + eventTags + " - via #GDGNGEvents";

                // Share event
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "GDG NG events");
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareIntent, "Share Via"));
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favButton.setImageResource(R.drawable.ic_action_favorite);

                // Save the event details
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                // Create a new map of values
                ContentValues values = new ContentValues();
                values.put(EventsDatabaseHelper.EVENTS_DURATION, duration);
                values.put(EventsDatabaseHelper.EVENTS_DATE, eventDate);
                values.put(EventsDatabaseHelper.EVENTS_NAME, eventName);
                values.put(EventsDatabaseHelper.EVENTS_TAGS, eventTags);
                values.put(EventsDatabaseHelper.EVENTS_TIME, eventTime);
                values.put(EventsDatabaseHelper.EVENTS_LONG_DESCRIPTION, longDescription);
                values.put(EventsDatabaseHelper.EVENTS_SHORT_DESCRIPTION, shortDescription);
                values.put(EventsDatabaseHelper.EVENTS_SPEAKERS, speakers);
                values.put(EventsDatabaseHelper.EVENTS_VENUE, venue);
                values.put(EventsDatabaseHelper.EVENTS_GDG, gdgName);

                // Insert the new row
                long newRowId = db.insert(EventsDatabaseHelper.EVENTS_TABLE_NAME,
                        null,
                        values);

                db.close();

                // Display success message
                Toast.makeText(getActivity(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        // Display the contents from the intent
        if (eventName != null) {
            ((TextView) rootView.findViewById(R.id.item_title)).setText(eventName);
        }
        if (duration != null) {
            ((TextView) rootView.findViewById(R.id.item_duration)).setText(duration);
        }
        if (eventDate != null) {
            ((TextView) rootView.findViewById(R.id.item_date)).setText(eventDate);
        }
        if (eventTags != null) {
            ((TextView) rootView.findViewById(R.id.item_tags)).setText(eventTags);
        }
        if (eventTime != null) {
            ((TextView) rootView.findViewById(R.id.item_time)).setText(DateParser.formattedTime(eventTime));
        }
        if (longDescription != null) {
            ((TextView) rootView.findViewById(R.id.item_description)).setText(longDescription);
        }
        if (speakers != null) {
            ((TextView) rootView.findViewById(R.id.item_speakers)).setText(speakers);
        }
        if (venue != null) {
            ((TextView) rootView.findViewById(R.id.item_venue)).setText(venue);
        }

        return rootView;
    }
}
