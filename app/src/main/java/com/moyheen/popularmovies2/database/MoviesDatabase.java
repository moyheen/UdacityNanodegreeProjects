package com.moyheen.popularmovies2.database;

import com.orm.SugarRecord;

/**
 * Created by moyinoluwa on 9/24/15.
 * Creates a new column for the list of movies with Sugar
 */
public class MoviesDatabase extends SugarRecord<MoviesDatabase> {
    String data;

    public MoviesDatabase() {
    }

    public MoviesDatabase(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }
}
