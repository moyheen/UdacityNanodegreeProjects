package com.moyinoluwa.gdgngevents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyinoluwa.gdgngevents.Organizers.Event;

import java.util.List;

/**
 * Created by moyinoluwa on 2/14/16.
 */
public class EventsRecyclerViewAdapter
        extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private final List<Event> eventList;
    private boolean mTwoPane;
    private Context mContext;

    public EventsRecyclerViewAdapter(List<Event> eventList, boolean mTwoPane, Context mContext) {
        this.mTwoPane = mTwoPane;
        this.mContext = mContext;
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.eventName.setText(eventList.get(position).getEvent_name());
        holder.shortDescription.setText(eventList.get(position).getShort_description());
        holder.venue.setText(eventList.get(position).getVenue());
        holder.eventDate.setText(eventList.get(position).getEvent_date());
        holder.eventTime.setText(DateParser.formattedTime(eventList.get(position).getEvent_time()));
        holder.duration.setText(eventList.get(position).getDuration());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    // Pass data to the fragment through bundles
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.EVENT_NAME, eventList.get(position).getEvent_name());
                    arguments.putString(ItemDetailFragment.SHORT_DESCRIPTION, eventList.get(position).getShort_description());
                    arguments.putString(ItemDetailFragment.LONG_DESCRIPTION, eventList.get(position).getLong_description());
                    arguments.putString(ItemDetailFragment.VENUE, eventList.get(position).getVenue());
                    arguments.putString(ItemDetailFragment.EVENT_DATE, eventList.get(position).getEvent_date());
                    arguments.putString(ItemDetailFragment.EVENT_TIME, eventList.get(position).getEvent_time());
                    arguments.putString(ItemDetailFragment.DURATION, eventList.get(position).getDuration());
                    arguments.putString(ItemDetailFragment.SPEAKERS, eventList.get(position).getSpeakers());
                    arguments.putString(ItemDetailFragment.EVENT_TAGS, eventList.get(position).getEvent_tags());

                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    // Pass data to the Activity through intents
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);

                    intent.putExtra(ItemDetailFragment.EVENT_NAME, eventList.get(position).getEvent_name());
                    intent.putExtra(ItemDetailFragment.SHORT_DESCRIPTION, eventList.get(position).getShort_description());
                    intent.putExtra(ItemDetailFragment.LONG_DESCRIPTION, eventList.get(position).getLong_description());
                    intent.putExtra(ItemDetailFragment.VENUE, eventList.get(position).getVenue());
                    intent.putExtra(ItemDetailFragment.EVENT_DATE, eventList.get(position).getEvent_date());
                    intent.putExtra(ItemDetailFragment.EVENT_TIME, eventList.get(position).getEvent_time());
                    intent.putExtra(ItemDetailFragment.DURATION, eventList.get(position).getDuration());
                    intent.putExtra(ItemDetailFragment.SPEAKERS, eventList.get(position).getSpeakers());
                    intent.putExtra(ItemDetailFragment.EVENT_TAGS, eventList.get(position).getEvent_tags());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView eventName;
        private TextView shortDescription;
        private TextView venue;
        private TextView eventDate;
        private TextView eventTime;
        private TextView duration;
        private CardView container;

        public ViewHolder(View view) {
            super(view);

            eventName = (TextView) view.findViewById(R.id.event_name);
            shortDescription = (TextView) view.findViewById(R.id.short_description);
            venue = (TextView) view.findViewById(R.id.venue);
            eventDate = (TextView) view.findViewById(R.id.event_date);
            eventTime = (TextView) view.findViewById(R.id.event_time);
            duration = (TextView) view.findViewById(R.id.duration);
            container = (CardView) view.findViewById(R.id.event_list_container);
        }
    }
}
