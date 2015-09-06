package com.moyheen.popularmovies1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Movie movie;
    private TextView movieTitle;
    private ImageView moviePoster;
    private TextView movieYear;
    private TextView movieRating;
    private TextView movieDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        movieTitle = (TextView) findViewById(R.id.movie_title);
        moviePoster = (ImageView) findViewById(R.id.movie_poster);
        movieYear = (TextView) findViewById(R.id.movie_year);
        movieRating = (TextView) findViewById(R.id.movie_rating);
        movieDescription = (TextView) findViewById(R.id.movie_desc);

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
        if (bundle != null) {
            String data = bundle.getString("movies");
            try {
                movie = new Movie(new JSONObject(data));

                // Loads the image with the Picasso library
                Picasso.with(this)
                        .load(Path.BASE_IMAGE_URL + movie.getProperty("poster_path").toString())
                        .into(moviePoster);

                // Sets the values for the views with data from the Bundle received
                movieTitle.setText(movie.getProperty("original_title").toString());
                movieYear.setText(movie.getProperty("release_date").toString().substring(0, 4));
                movieRating.setText(movie.getProperty("vote_average").toString() + "/10");
                movieDescription.setText(movie.getProperty("overview").toString());
            } catch (JSONException exception) {

            }
        }
    }
}
