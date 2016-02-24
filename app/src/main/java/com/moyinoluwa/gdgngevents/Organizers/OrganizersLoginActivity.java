package com.moyinoluwa.gdgngevents.Organizers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.moyinoluwa.gdgngevents.GDGOrganizer;
import com.moyinoluwa.gdgngevents.NetworkConnection;
import com.moyinoluwa.gdgngevents.R;

public class OrganizersLoginActivity extends AppCompatActivity {
    private TextView username;
    private TextView password;
    private Button submitButton;
    private NetworkConnection networkConnection;
    private Firebase firebaseRef;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizers_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getTitle());

        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        submitButton = (Button) findViewById(R.id.button);

        // Initialize a Firebase connection
        firebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        networkConnection = new NetworkConnection(this);
        sharedPreferences = getSharedPreferences("gdg_events", Context.MODE_PRIVATE);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks for empty fields
                if (username.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty()) {
                    Toast.makeText(OrganizersLoginActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (networkConnection.isInternetOn()) {
                        final String usernameValue = username.getText().toString();
                        final String passwordValue = password.getText().toString();

                        firebaseRef.child("gdg").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean authPassed = false;
                                String organizerName = "";
                                String gdg = "";

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    GDGOrganizer gdgOrganizer = postSnapshot.getValue(GDGOrganizer.class);

                                    // Validates the login email and password
                                    if (gdgOrganizer.getOrganizer_email().equals(usernameValue) &&
                                            gdgOrganizer.getGdg().equals(passwordValue)) {
                                        authPassed = true;
                                        gdg = gdgOrganizer.getGdg();
                                        organizerName = gdgOrganizer.getOrganizer_name();
                                        break;
                                    }
                                }

                                if (authPassed) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("organizer_gdg", gdg);
                                    editor.putString("organizer_name", organizerName);
                                    editor.commit();

                                    Intent i = new Intent(OrganizersLoginActivity.this, OrganizerEventDetailActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(OrganizersLoginActivity.this, "Login failed. Please check your username or password and try again.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(OrganizersLoginActivity.this, getResources().getString(R.string.no_network_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
