package com.moyheen.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by moyheen on 9/5/2015.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private static Context mContext;
    List<Movie> movieList;

    // Constructor for the MoviesAdapter class
    public MoviesAdapter(Context context, List<Movie> movieList) {
        this.movieList = movieList;
        this.mContext = context;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_recycler_view, parent, false);
        final MoviesViewHolder moviesViewHolder = new MoviesViewHolder(view);

        return moviesViewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, final int position) {

        // Loads the image with the Picasso library
        Picasso.with(mContext)
                .load(Path.BASE_IMAGE_URL + movieList.get(position).getProperty("poster_path").toString())
                .placeholder(R.drawable.abc_ic_go_search_api_mtrl_alpha)
                .error(R.drawable.abc_btn_check_material)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onClick(View view) {

                Intent i = new Intent(mContext, MovieDetailsActivity.class);
                // Passes data for the specific movie selected into the MovieDetails activity
                bundle.putSerializable("movies", movieList.get(position).toString());
                i.putExtras(bundle);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movieImage)
        ImageView imageView;

        public MoviesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
