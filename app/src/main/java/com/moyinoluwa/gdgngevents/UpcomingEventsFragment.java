package com.moyinoluwa.gdgngevents;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.batch.android.Batch;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.moyinoluwa.gdgngevents.Organizers.Event;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingEventsFragment extends Fragment {
    private View v;
    private boolean mTwoPane;
    public static final List<Event> eventList = new ArrayList<Event>();
    public static final List<GDGOrganizer> organizerList = new ArrayList<GDGOrganizer>();
    private TextView eventType;
    private LinearLayout gdgList;
    private TextView noGdgAvailableText;
    private TextView gdgIntroText;
    private Spinner gdgSpinner;
    private Button gdgSubmit;
    private TextView noEventsAvailable;
    private EventsRecyclerViewAdapter mEventsRecyclerViewAdapter;
    private NetworkConnection networkConnection;
    private Firebase mFirebaseEventRef;
    private Firebase mFirebaseRef;
    private View recyclerView;
    private boolean firstTimer;
    private String gdg;
    private SharedPreferences sharedPreferences;
    public static final String ACTION_DATA_UPDATED =
            "com.moyinoluwa.gdgngevents.ACTION_DATA_UPDATED";

    public UpcomingEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_upcoming_events, container, false);

        gdgList = (LinearLayout) v.findViewById(R.id.gdg_list);
        noGdgAvailableText = (TextView) v.findViewById(R.id.no_gdgs_available);
        gdgIntroText = (TextView) v.findViewById(R.id.gdg_intro_text);
        gdgSpinner = (Spinner) v.findViewById(R.id.gdg_spinner);
        gdgSubmit = (Button) v.findViewById(R.id.submit);

        noEventsAvailable = (TextView) v.findViewById(R.id.no_events_available);
        eventType = (TextView) v.findViewById(R.id.event_type);

        // Initialize a Firebase connection
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        mFirebaseEventRef = new Firebase(getResources().getString(R.string.event_firebase_url));
        networkConnection = new NetworkConnection(getActivity());

        recyclerView = v.findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, v);

        sharedPreferences = getActivity().getSharedPreferences("gdg_events", Context.MODE_PRIVATE);
        firstTimer = sharedPreferences.getBoolean("first_time", true);

        // Check if the user is using the application for the first time
        // Ask for their preferred GDG is this is the case
        if (firstTimer) {
            gdgList.setVisibility(View.VISIBLE);
            noEventsAvailable.setVisibility(View.GONE);
            displayGDGList();
        } else {
            // Get the GDG to query for events
            gdg = sharedPreferences.getString("gdg", gdg);
            Activity activity = this.getActivity();
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
            if (toolbar != null) {
                String gdgName = sharedPreferences.getString("gdg", "GDG");
                toolbar.setTitle(gdgName + " Events");
            }
            displayEvents();
        }

        gdgSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the selected GDG
                SharedPreferences.Editor editor = sharedPreferences.edit();
                gdg = gdgSpinner.getSelectedItem().toString();

                editor.putString("gdg", gdg);
                editor.putBoolean("first_time", false);
                editor.apply();

                gdgList.setVisibility(View.GONE);
                noEventsAvailable.setVisibility(View.VISIBLE);
                displayEvents();
            }
        });

        return v;
    }

    /**
     * Set up the screen to display one or two views accordingly
     **/
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, View v) {
        if (v.findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mEventsRecyclerViewAdapter =
                new EventsRecyclerViewAdapter(eventList, mTwoPane, getActivity());
        recyclerView.setAdapter(mEventsRecyclerViewAdapter);
    }

    /**
     * Display the list of GDGs from Firebase
     **/
    private void displayGDGList() {
        if (networkConnection.isInternetOn()) {

            mFirebaseRef.child("gdg").addValueEventListener(new ValueEventListener() {
                // Retrieve new Groups as they are added to the database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {
                        organizerList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            GDGOrganizer gdgOrganizer = data.getValue(GDGOrganizer.class);
                            organizerList.add(gdgOrganizer);
                        }

                        if (organizerList.size() > 0) {
                            noGdgAvailableText.setVisibility(View.GONE);
                            gdgIntroText.setVisibility(View.VISIBLE);
                            gdgSpinner.setVisibility(View.VISIBLE);
                            gdgSubmit.setVisibility(View.VISIBLE);

                            String[] gdgNames = new String[organizerList.size()];

                            // Create an array of GDGs to populate a spinner
                            for (int i = 0; i < organizerList.size(); i++) {
                                gdgNames[i] = organizerList.get(i).getGdg();
                            }

                            // Populate a spinner with data from Firebase
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, gdgNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            gdgSpinner.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("UpcomingEventsFragment", "The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Display the list of upcoming events from Firebase based on the GDG selected
     **/
    private void displayEvents() {
        if (networkConnection.isInternetOn()) {

            // Log the user into Firebase anonymously to get a userid for Batch SDK
            mFirebaseRef.authAnonymously(new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // we've authenticated this session with your Firebase app
                    // Give a custom user identifier to Batch SDK
                    Batch.User.getEditor()
                            .setIdentifier(authData.getUid())
                            .save();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                }
            });

            mFirebaseEventRef.child(gdg).addValueEventListener(new ValueEventListener() {
                // Retrieve new events as they are added to the database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {
                        eventList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Event event = data.getValue(Event.class);
                            // Only add events to the list if the event date is in the future
                            if (DateParser.isUpcoming(event.getEvent_date(), event.getEvent_time())) {
                                eventList.add(event);
                            }
                        }

                        if (eventList.size() > 0) {
                            if (isAdded()) {
                                noEventsAvailable.setVisibility(View.GONE);
                                eventType.setText(getResources().getString(R.string.upcoming_events));
                                eventType.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                mEventsRecyclerViewAdapter.notifyDataSetChanged();
                                // Update the widget with the first value from the list
                                updateWidgets(eventList.get(0));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("UpcomingEventsFragment", "The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update the data displayed in the widget
     **/
    private void updateWidgets(Event event) {
        Context context = getContext();
        // Setting the package ensures that only components in our
        // com.moyinoluwa.gdgngevents will receive the broadcast
        // Save the selected GDG
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("widget_name", event.getEvent_name());
        editor.putString("widget_desc", event.getShort_description());
        editor.putString("widget_venue", event.getVenue());
        editor.putString("widget_date", event.getEvent_date());
        editor.putString("widget_time", event.getEvent_time());
        editor.putString("widget_duration", event.getDuration());
        editor.apply();


        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}
