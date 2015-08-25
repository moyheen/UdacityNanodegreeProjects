package com.moyheen.project0;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button spotify;
    Button scores;
    Button library;
    Button buildIt;
    Button xyzReader;
    Button capstone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spotify = (Button) findViewById(R.id.spotify);
        scores = (Button) findViewById(R.id.scores);
        library = (Button) findViewById(R.id.library);
        buildIt = (Button) findViewById(R.id.build_it);
        xyzReader = (Button) findViewById(R.id.xyz_reader);
        capstone = (Button) findViewById(R.id.capstone);
    }

    /**
     * Handles all the button clicks based on the function defimed in the layout.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.spotify):
                displayToast(getString(R.string.spotify_streamer));
                break;
            case (R.id.scores):
                displayToast(getString(R.string.scores_app));
                break;
            case (R.id.library):
                displayToast(getString(R.string.library_app));
                break;
            case (R.id.build_it):
                displayToast(getString(R.string.build_it));
                break;
            case (R.id.xyz_reader):
                displayToast(getString(R.string.xyz_reader));
                break;
            case (R.id.capstone):
                displayToast(getString(R.string.capstone));
                break;
        }
    }

    /**
     * Displays a new toast with unique text based on the button selected.
     */
    public void displayToast(String text) {
        String message = "This button will launch my " + text + " app!";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
