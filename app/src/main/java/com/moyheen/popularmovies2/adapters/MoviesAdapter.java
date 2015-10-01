package com.moyheen.popularmovies2.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.moyheen.popularmovies2.fragments.MoviesFragment;
import com.moyheen.popularmovies2.utility.Data;
import com.moyheen.popularmovies2.utility.Path;
import com.moyheen.popularmovies2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by moyheen on 9/5/2015.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private Context mContext;
    private List<Data> mMovieList;
    public static int sPosition = 0;
    private static MoviesFragment.Callback callback;

    public MoviesAdapter(Context context, List<Data> movieList, MoviesFragment.Callback callback) {
        this.mMovieList = movieList;
        this.mContext = context;
        this.callback = callback;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_recycler_view, parent, false);
        final MoviesViewHolder moviesViewHolder = new MoviesViewHolder(view);

        // Automatically displays the details for the first movie in the in a two-pane mode
        // Determines the screen dimensions first and then updates the details of the second fragment accordingly
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        // Checks whether the phone is in two pane mode to ensure that it displays the correct layouts
        if (smallestWidth >= 600 || mContext.getResources()
                .getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // Sets the default value for the Details Fragment to the first item in the list
            updateDetail(mMovieList.get(sPosition).toString(), 0L);
        }
        return moviesViewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, final int position) {
        // Loads the image with the Picasso library
        Picasso.with(mContext)
                .load(Path.BASE_IMAGE_URL + mMovieList.get(position).getProperty("poster_path").toString())
                .placeholder(R.drawable.abc_ic_go_search_api_mtrl_alpha)
                .error(R.drawable.abc_btn_check_material)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updateDetail(mMovieList.get(position).toString(), position);

                sPosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movieImage)
        ImageView imageView;

        public MoviesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * Populates the MovieDetails fragment based on the movie selected in the first fragment
     */
    public static void updateDetail(String data, long position) {
        callback.onItemSelected(data, position);
    }
}
