package com.moyheen.popularmovies2.database;

import com.moyheen.popularmovies2.database.MoviesDatabase;
import com.orm.SugarRecord;

/**
 * Created by moyinoluwa on 9/24/15.
 * Creates a new column for the list of trailers with Sugar
 */
public class TrailerDatabase extends SugarRecord<MoviesDatabase> {
    String data;

    public TrailerDatabase() {
    }

    public TrailerDatabase(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }
}