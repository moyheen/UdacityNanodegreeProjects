package com.moyheen.popularmovies1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moyheen on 9/5/2015.
 * Model class for the movies list
 */
public class Movie {
    private JSONObject data;

    public Movie(JSONObject data) {
        this.data = data;
    }

    /**
     * Returns the specific data from the object based on the key requested
     */
    public Object getProperty(String key) {
        try {
            if (data != null) {
                return data.get(key);
            } else {
                return "Not found";
            }
        } catch (JSONException exception) {
            return "Not found";
        }
    }

    /**
     * Converts the data to a String and returns it
     */
    public String toString() {
        return data.toString();
    }
}
