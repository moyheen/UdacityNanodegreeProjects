package com.moyinoluwa.gdgngevents.Organizers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.moyinoluwa.gdgngevents.NetworkConnection;
import com.moyinoluwa.gdgngevents.R;

import java.util.ArrayList;
import java.util.List;

public class OrganizerEventDetailActivity extends AppCompatActivity {
    private TextView noEvents;
    private RecyclerView mRecyclerView;
    private EventAdapter eventAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Event> eventList = new ArrayList<Event>();
    private NetworkConnection networkConnection;
    private Firebase mFirebaseRef;
    private String gdg = "";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("gdg_events", Context.MODE_PRIVATE);
        gdg = sharedPreferences.getString("organizer_gdg", "gdg");
        String organizerName = sharedPreferences.getString("organizer_name", "John Doe");

        String welcomeText = "Welcome, " + organizerName + "!\n\n" + getResources().getString(R.string.no_events_yet);

        // Initialize a Firebase connection
        mFirebaseRef = new Firebase(getResources().getString(R.string.event_firebase_url));
        networkConnection = new NetworkConnection(this);

        noEvents = (TextView) findViewById(R.id.no_events);
        noEvents.setText(welcomeText);

        mRecyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        eventAdapter = new EventAdapter(this, eventList);
        mRecyclerView.setAdapter(eventAdapter);

        // Load all the events from Firebase
        displayEvents();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrganizerEventDetailActivity.this, CreateEventsActivity.class);
                i.putExtra("organizer_gdg", gdg);
                startActivity(i);
            }
        });
    }

    private void displayEvents() {
        if (networkConnection.isInternetOn()) {
            // Gets the store GDG to be queried
            String gdg = sharedPreferences.getString("organizer_gdg", "gdg");

            mFirebaseRef.child(gdg).addValueEventListener(new ValueEventListener() {
                // Retrieve new posts as they are added to the database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {
                        noEvents.setVisibility(View.GONE);
                        eventList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Event event = data.getValue(Event.class);
                            eventList.add(event);
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("OrganizerEventDetail", "The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(OrganizerEventDetailActivity.this, getResources().getString(R.string.no_network_connection),
                    Toast.LENGTH_SHORT).show();
        }

    }
}
