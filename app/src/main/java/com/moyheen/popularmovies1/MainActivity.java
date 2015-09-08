package com.moyheen.popularmovies1;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_spinner)
    Spinner mSpinner;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.moviesView)
    RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private NetworkConnection networkConnection;
    private MoviesAdapter moviesAdapter;
    private List<Movie> moviesList = new ArrayList<Movie>();
    public final String DISCOVER_MOVIE = "/discover/movie";
    public final String POPULARITY_DESC = "popularity.desc";
    public final String VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String MOVIE_LIST_KEY = "movieListKey";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uses the Butterkinfe Library to find and automatically cast
        // the corresponding view in layout
        ButterKnife.bind(this);

        gridLayoutManager = new GridLayoutManager(this, 2);
        networkConnection = new NetworkConnection(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Displays a spinner with the movie listing options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.movie_sorting, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (mSpinner.getSelectedItem().toString().equals("Popular Movies")) {

                    if (savedInstanceState != null) {
                        useSavedState(savedInstanceState);
                        moviesList = useSavedState(savedInstanceState);
                        moviesAdapter = new MoviesAdapter(MainActivity.this, moviesList);
                    } else {
                        displayMovies(POPULARITY_DESC);
                        moviesAdapter = new MoviesAdapter(MainActivity.this, moviesList);
                    }


                } else if (mSpinner.getSelectedItem().toString().equals("Highest Rated Movies")) {

                    if (savedInstanceState != null) {
                        moviesList = useSavedState(savedInstanceState);
                        moviesAdapter = new MoviesAdapter(MainActivity.this, moviesList);
                    } else {
                        displayMovies(VOTE_AVERAGE_DESC);
                        moviesAdapter = new MoviesAdapter(MainActivity.this, moviesList);
                    }
                }
                mRecyclerView.setAdapter(moviesAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Downloads the movie listing from the server based on the selected category using the Ion Library
     */
    private void displayMovies(String sort_by) {
        // Sets up the screen to display a new set of movies
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        moviesList.clear();

        if (networkConnection.isInternetOn()) {
            Ion.with(this)
                    .load(Path.BASE_URL + DISCOVER_MOVIE + "?sort_by=" + sort_by + "&api_key=" + Path.API_KEY)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            mProgressBar.setVisibility(View.GONE);

                            if (e == null) {
                                mRecyclerView.setVisibility(View.VISIBLE);

                                JsonArray resultArray = result.get("results").getAsJsonArray();

                                for (int i = 0; i < resultArray.size(); i++) {
                                    JsonObject resultObject = resultArray.get(i).getAsJsonObject();
                                    // Adds the data from the result to the movie list
                                    try {
                                        JSONObject movieData = new JSONObject(resultObject.toString());
                                        moviesList.add(new Movie(movieData));
                                    } catch (JSONException exception) {
                                    }
                                }
                                moviesAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Saves the current list of movies
        outState.putParcelableArrayList(MOVIE_LIST_KEY, (ArrayList<? extends Parcelable>) (ArrayList) moviesList);

        super.onSaveInstanceState(outState);
    }

    /**
     * Uses the data in the bundle to set the views
     */
    private List useSavedState(Bundle savedInstanceState) {
        // Restore values from a saved state
        moviesList = (ArrayList<Movie>) savedInstanceState.get(MOVIE_LIST_KEY);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        return moviesList;
    }
}
