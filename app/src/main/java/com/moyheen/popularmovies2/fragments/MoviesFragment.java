package com.moyheen.popularmovies2.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.moyheen.popularmovies2.R;
import com.moyheen.popularmovies2.adapters.MoviesAdapter;
import com.moyheen.popularmovies2.database.MoviesDatabase;
import com.moyheen.popularmovies2.utility.Data;
import com.moyheen.popularmovies2.utility.NetworkConnection;
import com.moyheen.popularmovies2.utility.Path;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends android.support.v4.app.Fragment {
    View rootView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.moviesView)
    RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private NetworkConnection networkConnection;
    private MoviesAdapter moviesAdapter;
    private static List<Data> sMoviesList = new ArrayList<Data>();
    public final String DISCOVER_MOVIE = "/discover/movie";
    public final String POPULARITY_DESC = "popularity.desc";
    public final String VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String MOVIE_LIST_KEY = "movieListKey";
    public static final String SELECTED_KEY = "selectedKey";
    public static String sSelectedSpinnerItemName = "Popular Movies";
    private static Callback listener;

    public MoviesFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        // Uses the Butterknife Library to find and automatically cast
        // the corresponding view in layout
        ButterKnife.bind(this, rootView);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        networkConnection = new NetworkConnection(getActivity());

        mRecyclerView.setLayoutManager(gridLayoutManager);

        Bundle bundle = this.getArguments();
        setSelectedSpinnerItemName(bundle.getString("sSelectedSpinnerItemName"));

        changeMovieDisplay();

        return rootView;
    }

    /**
     * Downloads the movie listing from the server based on the selected category using the Ion Library
     */
    private void displayMovies(String sort_by) {
        // Sets up the screen to display a new set of movies
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

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

                                sMoviesList.clear();

                                for (int i = 0; i < resultArray.size(); i++) {
                                    JsonObject resultObject = resultArray.get(i).getAsJsonObject();
                                    // Adds the sData from the result to the movie list
                                    try {
                                        JSONObject movieData = new JSONObject(resultObject.toString());
                                        sMoviesList.add(new Data(movieData));
                                    } catch (JSONException exception) {
                                        exception.printStackTrace();
                                    }
                                }
                                moviesAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    /**
     * Displays the favorite movies from the database
     */
    private void displayFavoriteMovies() {
        // Sets up the screen to display a new set of favorite movies
        sMoviesList.clear();

        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        // Gets the amount of data in the database
        long count = MoviesDatabase.count(MoviesDatabase.class, null, null);

        if (count == 0) {
            Toast.makeText(getActivity(), "You have no favorite movies yet", Toast.LENGTH_SHORT).show();
        } else {
            // Iterates through the database, converts the items to a JSONObject and adds it to the list
            for (long i = 1L; i <= count; i++) {
                String data = (MoviesDatabase.findById(MoviesDatabase.class, i).getData());

                try {
                    JSONObject movieData = new JSONObject(data);
                    sMoviesList.add(new Data(movieData));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        changeMovieDisplay();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            listener = (Callback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * MoviesFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String data, long position);
    }

    public void setSelectedSpinnerItemName(String selectedSpinnerItemName) {
        this.sSelectedSpinnerItemName = selectedSpinnerItemName;
    }

    public String getSelectedSpinnerItem() {
        return sSelectedSpinnerItemName;
    }

    /**
     * Changes the movie display based on the selected spinner item
     */
    public void changeMovieDisplay() {

        // Populates the movies list with data from the server. Uses data from the database for the Fvaorites item
        if (getSelectedSpinnerItem().equals(getResources().getString(R.string.popular_movies))) {

            displayMovies(POPULARITY_DESC);
            moviesAdapter = new MoviesAdapter(getActivity(), sMoviesList, listener);

        } else if (getSelectedSpinnerItem().equals(getResources().getString(R.string.rated_movies))) {

            displayMovies(VOTE_AVERAGE_DESC);
            moviesAdapter = new MoviesAdapter(getActivity(), sMoviesList, listener);

        } else {
            displayFavoriteMovies();
            moviesAdapter = new MoviesAdapter(getActivity(), sMoviesList, listener);
            moviesAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(moviesAdapter);
    }
}
