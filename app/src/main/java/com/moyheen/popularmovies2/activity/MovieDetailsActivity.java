package com.moyheen.popularmovies2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.moyheen.popularmovies2.fragments.MovieDetailsFragment;
import com.moyheen.popularmovies2.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Gets the data from the previous activity
        Bundle bundle = getIntent().getExtras();

        // Opens up the default fragment associated with the Activity
        final MovieDetailsFragment moviesDetailsFragment = new MovieDetailsFragment();
        moviesDetailsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, moviesDetailsFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent ac;//tivity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            // Shares the first trailer in the trailer list
            case (R.id.action_share):
                if (MovieDetailsFragment.trailerUrl == null) {
                    Toast.makeText(this, "No trailer avaliable", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, MovieDetailsFragment.trailerUrl);
                    startActivity(Intent.createChooser(intent, "Share with"));
                }
                break;
        }

        //noinspection SimplifiableIfStatement
        return true;
    }
}
