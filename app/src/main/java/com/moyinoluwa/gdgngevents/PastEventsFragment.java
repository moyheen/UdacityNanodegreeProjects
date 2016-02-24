package com.moyinoluwa.gdgngevents;


import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

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
public class PastEventsFragment extends Fragment {
    private View v;
    private boolean mTwoPane;
    public static final List<Event> eventList = new ArrayList<Event>();
    private TextView eventType;
    private TextView noEventsAvailable;
    private EventsRecyclerViewAdapter mEventsRecyclerViewAdapter;
    private NetworkConnection networkConnection;
    private Firebase mFirebaseRef;
    private View recyclerView;
    private SharedPreferences sharedPreferences;


    public PastEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_past_events, container, false);

        // Initialize a Firebase connection
        mFirebaseRef = new Firebase(getResources().getString(R.string.event_firebase_url));
        networkConnection = new NetworkConnection(getActivity());

        noEventsAvailable = (TextView) v.findViewById(R.id.no_events_available);
        eventType = (TextView) v.findViewById(R.id.event_type);

        sharedPreferences = getActivity().getSharedPreferences("gdg_events", Context.MODE_PRIVATE);

        recyclerView = v.findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, v);

        // Load events from Firebase
        displayEvents();

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
     * Display past events data in the views
     **/
    private void displayEvents() {
        if (networkConnection.isInternetOn()) {

            // Get the saved GDG to query for events
            String gdg = sharedPreferences.getString("gdg", "gdg");

            Activity activity = this.getActivity();
            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(gdg + " Events");
            }

            mFirebaseRef.child(gdg).addValueEventListener(new ValueEventListener() {
                // Retrieve new events as they are added to the database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {
                        eventList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Event event = data.getValue(Event.class);
                            // Only add events to the list if the event date is past
                            if (!DateParser.isUpcoming(event.getEvent_date(), event.getEvent_time())) {
                                eventList.add(event);
                            }
                        }
                        if (eventList.size() > 0) {
                            if (isAdded()) {
                                noEventsAvailable.setVisibility(View.GONE);
                                eventType.setText(getResources().getString(R.string.past_events));
                                eventType.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                mEventsRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("PastEventsFragment", "The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_network_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
