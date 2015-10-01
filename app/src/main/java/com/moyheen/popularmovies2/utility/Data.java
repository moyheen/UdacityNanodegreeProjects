package com.moyheen.popularmovies2.utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by moyheen on 9/5/2015.
 * Model class for all the lists used
 */
public class Data implements Serializable {
    private transient JSONObject data;

    public Data(JSONObject data) {
        this.data = data;
    }

    /**
     * Returns the specific sData from the object based on the key requested
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
     * Converts the data into a String and returns it
     */
    public String toString() {
        return data.toString();
    }
}
