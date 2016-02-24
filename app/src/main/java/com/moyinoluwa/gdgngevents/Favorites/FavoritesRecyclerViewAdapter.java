package com.moyinoluwa.gdgngevents.Favorites;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyinoluwa.gdgngevents.ItemDetailActivity;
import com.moyinoluwa.gdgngevents.ItemDetailFragment;
import com.moyinoluwa.gdgngevents.R;


/**
 * Created by moyinoluwa on 2/14/16.
 */
public class FavoritesRecyclerViewAdapter
        extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.ViewHolder> {

    private Cursor cursor;
    private boolean mTwoPane;
    private Context mContext;

    // These indices must match the projection
    private static final int INDEX_EVENTS_ID = 0;
    private static final int INDEX_EVENTS_DURATION = 1;
    private static final int INDEX_EVENTS_DATE = 2;
    private static final int INDEX_EVENTS_NAME = 3;
    private static final int INDEX_EVENTS_TAGS = 4;
    private static final int INDEX_EVENTS_TIME = 5;
    private static final int INDEX_LONG_DESC = 6;
    private static final int INDEX_SHORT_DESC = 7;
    private static final int INDEX_SPEAKERS = 8;
    private static final int INDEX_VENUE = 9;
    private static final int INDEX_GDG_NAME = 10;

    public FavoritesRecyclerViewAdapter(Cursor cursor, boolean mTwoPane, Context mContext) {
        this.mTwoPane = mTwoPane;
        this.mContext = mContext;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        cursor.moveToPosition(position);

        // Extract the event data from the Cursor
        final String eventName = cursor.getString(INDEX_EVENTS_NAME);
        final String shortDesc = cursor.getString(INDEX_SHORT_DESC);
        final String longDesc = cursor.getString(INDEX_LONG_DESC);
        final String venue = cursor.getString(INDEX_VENUE);
        final String date = cursor.getString(INDEX_EVENTS_DATE);
        final String time = cursor.getString(INDEX_EVENTS_TIME);
        final String duration = cursor.getString(INDEX_EVENTS_DURATION);
        final String speakers = cursor.getString(INDEX_SPEAKERS);
        final String tags = cursor.getString(INDEX_EVENTS_TAGS);
        final String gdgName = cursor.getString(INDEX_GDG_NAME);

        // Move the data to the views
        holder.eventName.setText(eventName);
        holder.shortDescription.setText(shortDesc);
        holder.venue.setText(venue);
        holder.eventDate.setText(date);
        holder.eventTime.setText(time);
        holder.duration.setText(duration);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    // Pass data to the fragment through bundles
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.EVENT_NAME, eventName);
                    arguments.putString(ItemDetailFragment.SHORT_DESCRIPTION, shortDesc);
                    arguments.putString(ItemDetailFragment.LONG_DESCRIPTION, longDesc);
                    arguments.putString(ItemDetailFragment.VENUE, venue);
                    arguments.putString(ItemDetailFragment.EVENT_DATE, date);
                    arguments.putString(ItemDetailFragment.EVENT_TIME, time);
                    arguments.putString(ItemDetailFragment.DURATION, duration);
                    arguments.putString(ItemDetailFragment.SPEAKERS, speakers);
                    arguments.putString(ItemDetailFragment.EVENT_TAGS, tags);
                    arguments.putString(ItemDetailFragment.EVENT_GDG, gdgName);

                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    // Pass data to the Activity through intents
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);

                    intent.putExtra(ItemDetailFragment.EVENT_NAME, eventName);
                    intent.putExtra(ItemDetailFragment.SHORT_DESCRIPTION, shortDesc);
                    intent.putExtra(ItemDetailFragment.LONG_DESCRIPTION, longDesc);
                    intent.putExtra(ItemDetailFragment.VENUE, cursor.getString(INDEX_VENUE));
                    intent.putExtra(ItemDetailFragment.EVENT_DATE, date);
                    intent.putExtra(ItemDetailFragment.EVENT_TIME, time);
                    intent.putExtra(ItemDetailFragment.DURATION, duration);
                    intent.putExtra(ItemDetailFragment.SPEAKERS, speakers);
                    intent.putExtra(ItemDetailFragment.EVENT_TAGS, tags);
                    intent.putExtra(ItemDetailFragment.EVENT_GDG, gdgName);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor dataCursor) {
        if (cursor == dataCursor) {
            return null;
        }

        Cursor oldCursor = cursor;
        this.cursor = dataCursor;
        if (dataCursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
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
