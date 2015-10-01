package com.moyheen.popularmovies2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.moyheen.popularmovies2.adapters.MoviesAdapter;
import com.moyheen.popularmovies2.fragments.MovieDetailsFragment;
import com.moyheen.popularmovies2.R;
import com.moyheen.popularmovies2.fragments.MoviesFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {
    @Bind(R.id.toolbar)
    Toolbar mToolBar;
    @Bind(R.id.toolbar_spinner)
    Spinner mSpinner;

    public static String sSelectedSpinnerItemName;
    public static String sData;
    public MoviesFragment moviesFragment;
    private final String SELECTED_SPINNER_ITEM = "selectedSpinner";
    private final String MOVIES_FRAGMENT_TAG = "MFT";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sSelectedSpinnerItemName = getResources().getString(R.string.popular_movies);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);

        launchMoviesFragment(null, sSelectedSpinnerItemName);

        // Displays a spinner with the movie listing options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.movie_sorting, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        // Sets the sSelectedSpinnerItemName based on the Spinner item selected
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (mSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.popular_movies))) {
                    sSelectedSpinnerItemName = getResources().getString(R.string.popular_movies);
                } else if (mSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.rated_movies))) {
                    sSelectedSpinnerItemName = getResources().getString(R.string.rated_movies);
                } else if (mSpinner.getSelectedItem().toString().equals(getResources().getString(R.string.favorite_movies))) {
                    sSelectedSpinnerItemName = getResources().getString(R.string.favorite_movies);
                }
                getSupportActionBar().setTitle(sSelectedSpinnerItemName);

                launchMoviesFragment(savedInstanceState, sSelectedSpinnerItemName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onItemSelected(String data, long position) {

        Bundle bundle = new Bundle();
        bundle.putString("sData", data);
        bundle.putLong("position", position);

        // Launches the two-pane mode if the second fragment exists
        if (findViewById(R.id.movie_detail_container) != null) {

            final MovieDetailsFragment moviesDetailsFragment = new MovieDetailsFragment();
            moviesDetailsFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, moviesDetailsFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        } else {
            Intent i = new Intent(MainActivity.this, MovieDetailsActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is in two-pane mode.

        if (findViewById(R.id.movie_detail_container) != null) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Saves the movie fragment instance state
        getSupportFragmentManager().putFragment(outState, "mContent", moviesFragment);
        outState.putString(SELECTED_SPINNER_ITEM, sSelectedSpinnerItemName);
    }


    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        // Retrieves the stored instance state
        sSelectedSpinnerItemName = inState.getString(SELECTED_SPINNER_ITEM);
        launchMoviesFragment(inState, sSelectedSpinnerItemName);
    }

    /**
     * Opens a Movie fragment after checking for data from the savedInstanceState
     **/
    private void launchMoviesFragment(Bundle savedInstanceState, String sSelectedSpinnerItemName) {

        // Launches a new Fragment

        Bundle bundle = new Bundle();
        bundle.putString("sSelectedSpinnerItemName", sSelectedSpinnerItemName);

        moviesFragment = new MoviesFragment();
        moviesFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movies, moviesFragment, MOVIES_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}
