package com.moyinoluwa.gdgngevents.Organizers;

/**
 * Created by moyinoluwa on 2/16/16.
 */
public class Event {
    private String duration;
    private String event_date;
    private String event_name;
    private String event_tags;
    private String event_time;
    private String long_description;
    private String short_description;
    private String speakers;
    private String venue;

    public Event() {

    }

    public String getDuration() {
        return duration;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_tags() {
        return event_tags;
    }

    public String getEvent_time() {
        return event_time;
    }

    public String getLong_description() {
        return long_description;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getSpeakers() {
        return speakers;
    }

    public String getVenue() {
        return venue;
    }
}
