package com.moyinoluwa.gdgngevents.Champion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.moyinoluwa.gdgngevents.GDGOrganizer;
import com.moyinoluwa.gdgngevents.NetworkConnection;
import com.moyinoluwa.gdgngevents.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChampionListActivity extends AppCompatActivity {
    private Firebase mFirebase;
    private Firebase gdgFirebaseRef;
    private TextView noOrganizers;
    private TextView mGdgTextView;
    private TextView mOrganizerNameTextView;
    private TextView mOrganizerEmailTextView;
    private RecyclerView container;
    private NetworkConnection networkConnection;
    private View organizerInputView;
    private GdgAdapter gdgAdapter;
    private List<GDGOrganizer> gdgOrganizerArrayList = new ArrayList<GDGOrganizer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Firebase
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        gdgFirebaseRef = new Firebase(getResources().getString(R.string.gdg_firebase_url));

        noOrganizers = (TextView) findViewById(R.id.no_organizers);
        container = (RecyclerView) findViewById(R.id.champion_container);
        networkConnection = new NetworkConnection(this);

        container = (RecyclerView) findViewById(R.id.champion_container);
        assert container != null;

        // Load the data from Firebase
        getGdgData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Collect new GDG Organizer information
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ChampionListActivity.this, R.style.AppCompatAlertDialogStyle);
                organizerInputView = getLayoutInflater().inflate(R.layout.form_create_organizer, null);
                builder.setView(organizerInputView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGdgTextView = (TextView) organizerInputView.findViewById(R.id.gdg_name);
                        mOrganizerNameTextView = (TextView) organizerInputView.findViewById(R.id.lead_organizer_name);
                        mOrganizerEmailTextView = (TextView) organizerInputView.findViewById(R.id.lead_organizer_email);

                        // Check if the fields are empty before submitting them
                        if (mGdgTextView.getText().toString().isEmpty() ||
                                mOrganizerNameTextView.getText().toString().isEmpty() ||
                                mOrganizerEmailTextView.getText().toString().isEmpty()) {
                            Toast.makeText(ChampionListActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                        } else {
                            if (networkConnection.isInternetOn()) {
                                Map<String, String> event = new HashMap<String, String>();
                                event.put("organizer_name", mOrganizerNameTextView.getText().toString());
                                event.put("organizer_email", mOrganizerEmailTextView.getText().toString());
                                event.put("gdg", mGdgTextView.getText().toString());
                                mFirebase.child("gdg").push().setValue(event);

                            } else {
                                Toast.makeText(ChampionListActivity.this, getResources().getString(R.string.no_network_connection),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    /**
     * Listen to the data from Firebase
     **/
    private void getGdgData() {
        if (networkConnection.isInternetOn()) {

            gdgFirebaseRef.addValueEventListener(new ValueEventListener() {
                // Retrieve new posts as they are added to the database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {
                        gdgOrganizerArrayList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            GDGOrganizer gdgOrganizer = data.getValue(GDGOrganizer.class);
                            gdgOrganizerArrayList.add(gdgOrganizer);
                        }

                        if (gdgOrganizerArrayList.size() > 0) {
                            noOrganizers.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
                            gdgAdapter = new GdgAdapter(gdgOrganizerArrayList);
                            container.setAdapter(gdgAdapter);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e("ChampionListActivity", "The read failed: " + firebaseError.getMessage());
                }
            });
        } else {
            Toast.makeText(ChampionListActivity.this, getResources().getString(R.string.no_network_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
