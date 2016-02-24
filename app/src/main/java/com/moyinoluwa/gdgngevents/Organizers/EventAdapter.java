package com.moyinoluwa.gdgngevents.Organizers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyinoluwa.gdgngevents.DateParser;
import com.moyinoluwa.gdgngevents.R;

import java.util.List;

/**
 * Created by moyinoluwa on 2/16/16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private static Context mContext;
    List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.mContext = context;
        this.eventList = eventList;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_recycler_view, parent, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.eventName.setText(eventList.get(position).getEvent_name());
        holder.shortDescription.setText(eventList.get(position).getShort_description());
        holder.venue.setText(eventList.get(position).getVenue());
        holder.eventDate.setText(eventList.get(position).getEvent_date());
        holder.eventTime.setText(DateParser.formattedTime(eventList.get(position).getEvent_time()));
        holder.duration.setText(eventList.get(position).getDuration());
        holder.speakers.setText(eventList.get(position).getSpeakers());
        holder.eventTags.setText(eventList.get(position).getEvent_tags());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName;
        private TextView shortDescription;
        private TextView venue;
        private TextView eventDate;
        private TextView eventTime;
        private TextView duration;
        private TextView speakers;
        private TextView eventTags;

        public EventViewHolder(View view) {
            super(view);

            eventName = (TextView) view.findViewById(R.id.event_name);
            shortDescription = (TextView) view.findViewById(R.id.short_description);
            venue = (TextView) view.findViewById(R.id.venue);
            eventDate = (TextView) view.findViewById(R.id.event_date);
            eventTime = (TextView) view.findViewById(R.id.event_time);
            duration = (TextView) view.findViewById(R.id.duration);
            speakers = (TextView) view.findViewById(R.id.speakers);
            eventTags = (TextView) view.findViewById(R.id.event_tags);
        }
    }
}
