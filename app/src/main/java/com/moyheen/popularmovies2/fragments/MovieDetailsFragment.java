package com.moyheen.popularmovies2.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.moyheen.popularmovies2.R;
import com.moyheen.popularmovies2.database.MoviesDatabase;
import com.moyheen.popularmovies2.database.ReviewsDatabase;
import com.moyheen.popularmovies2.database.TrailerDatabase;
import com.moyheen.popularmovies2.utility.Data;
import com.moyheen.popularmovies2.utility.NetworkConnection;
import com.moyheen.popularmovies2.utility.Path;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends android.support.v4.app.Fragment {
    View rootView;
    @Bind(R.id.movie_title)
    TextView movieTitle;
    @Bind(R.id.movie_poster)
    ImageView moviePoster;
    @Bind(R.id.movie_year)
    TextView movieYear;
    @Bind(R.id.movie_rating)
    TextView movieRating;
    @Bind(R.id.favorite)
    Button favorite;
    @Bind(R.id.no_trailer)
    TextView noTrailer;
    @Bind(R.id.no_review)
    TextView noReview;
    @Bind(R.id.movie_desc)
    TextView movieDescription;
    @Bind(R.id.trailers_view)
    LinearLayout mLayoutTrailers;
    @Bind(R.id.reviews_view)
    LinearLayout mLayoutReviews;

    public static String trailerUrl;
    private NetworkConnection networkConnection;
    private List<Data> trailersList = new ArrayList<Data>();
    private List<Data> reviewsList = new ArrayList<Data>();
    public String movieId;
    private String detailsData;
    final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
    private long moviesPosition;

    public static long moviePosition;

    Data movie;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        // Binds the views with the Butterknife Library
        ButterKnife.bind(this, rootView);

        networkConnection = new NetworkConnection(getActivity());

        // Gets the sData from the previous activity
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.detailsData = bundle.getString("sData");
            this.moviesPosition = bundle.getLong("position");
        }

        try {
            movie = new Data(new JSONObject(detailsData));

            // Loads the image with the Picasso library
            Picasso.with(getActivity())
                    .load(Path.BASE_IMAGE_URL + movie.getProperty("poster_path").toString())
                    .placeholder(R.drawable.abc_btn_default_mtrl_shape)
                    .error(R.drawable.abc_btn_rating_star_off_mtrl_alpha)
                    .into(moviePoster);

            // Sets the values for the views with sData from the Bundle received
            movieId = movie.getProperty("id").toString();
            movieTitle.setText(movie.getProperty("original_title").toString());
            if (movie.getProperty("release_date").toString().length() != 0) {
                movieYear.setText(movie.getProperty("release_date").toString().substring(0, 4));
            }
            movieRating.setText(movie.getProperty("vote_average").toString() + "/10");
            movieDescription.setText(movie.getProperty("overview").toString());

            // Displays trailers and reviews from the database if the Favorites movies option is selected
            if (MoviesFragment.sSelectedSpinnerItemName.equals(getResources().getString(R.string.favorite_movies))) {
                // Sets up the screen to display a new set of movies
                trailersList.clear();
                reviewsList.clear();

                // Increments the movie from the Bundle passed from the main activity
                // because the Sugar Database starts from 1
                moviesPosition++;

                // Retrieves the sData from the Sugar Database Library
                String trailerData = (TrailerDatabase.findById(TrailerDatabase.class, moviesPosition).getData());
                String reviewData = (ReviewsDatabase.findById(ReviewsDatabase.class, moviesPosition).getData());

                // Populates both the Trailer and the Reviews with the sData from the database
                try {
                    JSONArray trailerDataArray = new JSONArray(trailerData);
                    JSONArray reviewDataArray = new JSONArray(reviewData);

                    for (int i = 0; i < trailerDataArray.length(); i++) {
                        JSONObject trailerDataObject = trailerDataArray.getJSONObject(i);
                        trailersList.add(new Data(trailerDataObject));
                    }

                    for (int i = 0; i < reviewDataArray.length(); i++) {
                        JSONObject reviewDataObject = reviewDataArray.getJSONObject(i);
                        reviewsList.add(new Data(reviewDataObject));
                    }
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            } else {
                // Displays the trailer and reviews from the server instead if the Favorites spinner
                // is not selected
                displayTrailers(movieId);

                // Displays the movie reviews
                displayReviews(movieId);
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }


        favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MoviesDatabase mDatabase = new MoviesDatabase(detailsData);
                mDatabase.save();

                // Saves the movie trailer to the database
                TrailerDatabase trailerDatabase = new TrailerDatabase(String.valueOf(trailersList));
                trailerDatabase.save();

                // Saves the movie reviews to the database
                ReviewsDatabase reviewDatabase = new ReviewsDatabase(String.valueOf(reviewsList));
                reviewDatabase.save();

                String message = movie.getProperty("original_title") +
                        " has been added to favorites";
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    /**
     * Downloads the trailers from the server using the Ion Library
     */
    private void displayTrailers(String movieId) {
        // Sets up the screen to display a new set of trailers
        trailersList.clear();

        if (networkConnection.isInternetOn()) {
            Ion.with(this)
                    .load(Path.BASE_URL + "/movie/" + movieId + "/videos?api_key=" + Path.API_KEY)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (e == null) {

                                JsonArray resultArray = result.get("results").getAsJsonArray();

                                for (int i = 0; i < resultArray.size(); i++) {
                                    JsonObject resultObject = resultArray.get(i).getAsJsonObject();
                                    // Adds the sData from the result to the trailers list
                                    try {
                                        JSONObject trailerData = new JSONObject(resultObject.toString());
                                        trailersList.add(new Data(trailerData));
                                    } catch (JSONException exception) {
                                        exception.printStackTrace();
                                    }
                                }

                                LayoutInflater inflater = (LayoutInflater) getActivity()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                if (trailersList.size() > 0) {
                                    noTrailer.setVisibility(View.GONE);

                                    // Sets the trailer url to the first one in the list
                                    trailerUrl = YOUTUBE_URL + trailersList.get(0).getProperty("key").toString();

                                    // Automatically populates the trailers with the sData from the trailer list
                                    for (int i = 0; i < trailersList.size(); i++) {
                                        View v = inflater.inflate(R.layout.trailer_recycler_view, null);

                                        // Builds the youtube url for the current trailer to be shared
                                        final String key = trailersList.get(i).getProperty("key").toString();

                                        TextView textView = (TextView) v.findViewById(R.id.trailer_text);
                                        textView.setText("Trailer " + i);

                                        ImageButton imageButton = (ImageButton) v.findViewById(R.id.trailer_button);

                                        imageButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(
                                                        Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + key)));
                                            }
                                        });
                                        mLayoutTrailers.addView(v);
                                    }
                                } else {
                                    // Resets the value of the trailer url if there is no trailer available
                                    trailerUrl = null;
                                }
                            }
                        }
                    });
        }
    }

    /**
     * Downloads the reviews from the server using the Ion Library
     */
    private void displayReviews(String movieId) {
        // Sets up the screen to display a new set of reviews
        reviewsList.clear();

        if (networkConnection.isInternetOn()) {
            Ion.with(this)
                    .load(Path.BASE_URL + "/movie/" + movieId + "/reviews?api_key=" + Path.API_KEY)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            if (e == null) {

                                JsonArray resultArray = result.get("results").getAsJsonArray();

                                for (int i = 0; i < resultArray.size(); i++) {
                                    JsonObject resultObject = resultArray.get(i).getAsJsonObject();
                                    // Adds the sData from the result to the trailers list
                                    try {
                                        JSONObject reviewData = new JSONObject(resultObject.toString());
                                        reviewsList.add(new Data(reviewData));
                                    } catch (JSONException exception) {
                                        exception.printStackTrace();
                                    }
                                }

                                LayoutInflater inflater = (LayoutInflater) getActivity()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                if (reviewsList.size() > 0) {
                                    noReview.setVisibility(View.GONE);
                                    // Automatically populates the reviews with the sData from the review list
                                    for (int i = 0; i < reviewsList.size(); i++) {
                                        View v = inflater.inflate(R.layout.reviews_recycler_view, null);

                                        TextView author = (TextView) v.findViewById(R.id.author);
                                        author.setText(reviewsList.get(i).getProperty("author").toString());

                                        TextView content = (TextView) v.findViewById(R.id.content);
                                        content.setText(reviewsList.get(i).getProperty("content").toString());

                                        mLayoutReviews.addView(v);
                                    }
                                }
                            }
                        }
                    });
        }
    }
}
