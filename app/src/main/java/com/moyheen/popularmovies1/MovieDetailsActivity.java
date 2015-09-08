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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.movie_title)
    TextView movieTitle;
    @Bind(R.id.movie_poster)
    ImageView moviePoster;
    @Bind(R.id.movie_year)
    TextView movieYear;
    @Bind(R.id.movie_rating)
    TextView movieRating;
    @Bind(R.id.movie_desc)
    TextView movieDescription;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Binds the views with the Butterknife Library
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
        if (bundle != null) {
            String data = String.valueOf(bundle.getSerializable("movies"));
            try {
                movie = new Movie(new JSONObject(data));

                // Loads the image with the Picasso library
                Picasso.with(this)
                        .load(Path.BASE_IMAGE_URL + movie.getProperty("poster_path").toString())
                        .placeholder(R.drawable.abc_btn_default_mtrl_shape)
                        .error(R.drawable.abc_btn_rating_star_off_mtrl_alpha)
                        .into(moviePoster);

                // Sets the values for the views with data from the Bundle received
                movieTitle.setText(movie.getProperty("original_title").toString());
                movieYear.setText(movie.getProperty("release_date").toString().substring(0, 4));
                movieRating.setText(movie.getProperty("vote_average").toString() + "/10");
                movieDescription.setText(movie.getProperty("overview").toString());
            } catch (JSONException exception) {
                movie = null;
            }
        }
    }
}
