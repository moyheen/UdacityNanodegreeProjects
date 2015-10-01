package com.moyheen.popularmovies2.database;

import com.orm.SugarRecord;

/**
 * Created by moyinoluwa on 9/24/15.
 * Creates a new column for the list of reviews with Sugar
 */
public class ReviewsDatabase extends SugarRecord<MoviesDatabase> {
    String data;

    public ReviewsDatabase() {
    }

    public ReviewsDatabase(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }
}