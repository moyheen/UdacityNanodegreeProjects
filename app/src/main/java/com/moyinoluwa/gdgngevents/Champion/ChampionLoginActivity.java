package com.moyinoluwa.gdgngevents.Champion;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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
import com.moyinoluwa.gdgngevents.NetworkConnection;
import com.moyinoluwa.gdgngevents.R;

public class ChampionLoginActivity extends AppCompatActivity {
    private Firebase mFirebase;
    private TextView mTextView;
    private TextInputLayout mTextInputLayout;
    private Button loginButton;
    private NetworkConnection networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getTitle());

        // Initialize a connection to Firebase
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        networkConnection = new NetworkConnection(this);

        mTextView = (TextView) findViewById(R.id.password);
        mTextInputLayout = (TextInputLayout) findViewById(R.id.password_layout);
        loginButton = (Button) findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTextView.getText().toString().isEmpty()) {
                    mTextInputLayout.setError(getResources().getString(R.string.organizer_empty_login));
                } else {
                    if (networkConnection.isInternetOn()) {

                        // Listen to data on the "champion" node in Firebase
                        mFirebase.child("champion").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                String correctPassword = snapshot.getValue().toString();
                                // Validates the password by comparing with value from the Firebase Server
                                if (isPasswordCorrect(correctPassword)) {
                                    // Logs the user in
                                    mTextView.setText("");
                                    mTextInputLayout.setError("");
                                    Intent i = new Intent(ChampionLoginActivity.this, ChampionListActivity.class);
                                    startActivity(i);
                                } else {
                                    mTextInputLayout.setError(getResources().getString(R.string.organizer_wrong_login));
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(ChampionLoginActivity.this, getResources().getString(R.string.no_network_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Validates the password
     **/
    private boolean isPasswordCorrect(String correctPassword) {
        return correctPassword.equals(mTextView.getText().toString());
    }
}
