package com.moyinoluwa.mylibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LibraryActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mTextView = (TextView) findViewById(R.id.joke_text);

        // Retrieves the joke from the intent
        String joke = getIntent().getStringExtra("joke");

        // Displays the joke
        mTextView.setText(joke);
    }
}
