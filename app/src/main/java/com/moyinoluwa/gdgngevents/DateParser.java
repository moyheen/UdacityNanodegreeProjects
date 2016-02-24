package com.moyinoluwa.gdgngevents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by moyinoluwa on 2/17/16.
 */
public class DateParser {

    /**
     * Transforms the date and time to a SimpleDateFormat
     **/
    public static boolean isUpcoming(String rawDate, String rawTime) {
        boolean upcoming = false;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
        String dateInString = createDateInString(rawDate, rawTime);
        Date date = null;
        // Converts the date to a SimpleDateFormat
        try {
            date = simpleDateFormat.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Sets the time for the previously formatted date
        Calendar cEnd = Calendar.getInstance(Locale.getDefault());
        cEnd.clear();
        cEnd.setTime(date);

        // Gets the current time
        Calendar cCurrent = Calendar.getInstance(Locale.getDefault());

        // Compares the two dates to determine upcoming and past events
        if (cCurrent.after(cEnd)) {
            upcoming = false;
        } else if (cCurrent.before(cEnd)) {
            upcoming = true;
        }

        return upcoming;
    }

    /**
     * Transforms the date and time to a Date Format
     **/
    private static String createDateInString(String date, String time) {

        return date + " " + time + ":00";
    }

    /**
     * Transforms the time from a 24hr format to a 12 hour format
     **/
    public static String formattedTime(String time) {
        // Extract the hour which is the first two characters of the string
        String hour = time.substring(0, 2);
        int hourInt = Integer.parseInt(hour);

        String AM_PM = (hourInt < 12) ? " AM" : " PM";

        // Gets the 12 hour format of the hour
        if (hourInt > 12) {
            hourInt = hourInt - 12;
        }

        // Extract the minute which is the last two characters of the string
        String minute = time.substring(3, 5);

        return String.valueOf(hourInt) + ":" + minute + AM_PM;
    }
}
